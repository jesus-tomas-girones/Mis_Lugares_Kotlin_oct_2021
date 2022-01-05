package com.example.mislugares2021kotlin.casos_uso

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.mislugares2021kotlin.datos.RepositorioLugares
import com.example.mislugares2021kotlin.modelo.GeoPunto
import com.example.mislugares2021kotlin.modelo.Lugar
import com.example.mislugares2021kotlin.presentacion.AcercaDeActivity
import com.example.mislugares2021kotlin.presentacion.EdicionLugarActivity
import com.example.mislugares2021kotlin.presentacion.VistaLugarActivity
import java.io.File
import java.io.IOException
import android.R.attr.data
import android.annotation.SuppressLint


class CasosUsoLugar(
    val actividad: Activity,
    val lugares: RepositorioLugares
) {
    init {
        setGaleriaLauncher()
        setTomarFotoLauncher()
    }

    // OPERACIONES BÁSICAS
    fun mostrar(pos: Int) {
        val i = Intent(actividad, VistaLugarActivity::class.java)
        i.putExtra("pos", pos);
        actividad.startActivity(i);
    }

    fun editar(pos: Int, launcher: ActivityResultLauncher<Intent>) {
        val i = Intent(actividad, EdicionLugarActivity::class.java)
        i.putExtra("pos", pos);
        launcher.launch(i);
    }

    fun guardar(id: Int, nuevoLugar: Lugar) {
        lugares.actualiza(id, nuevoLugar)
    }

    fun borrar(id: Int) {
        lugares.borrar(id)
        actividad.finish()
    }

    // INTENCIONES
    fun compartir(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$(lugar.nombre) - $(lugar.url)")
        })

    fun llamarTelefono(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.telefono))
    )

    fun verPgWeb(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse(lugar.url))
    )

    fun verMapa(lugar: Lugar) {
        val lat = lugar.posicion.latitud
        val lon = lugar.posicion.longitud
        val uri = if (lugar.posicion != GeoPunto.SIN_POSICION)
            Uri.parse("geo:$lat,$lon")
        else Uri.parse("geo:0,0?q=" + lugar.direccion)
        actividad.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    // FOTOGRAFÍAS
    lateinit var fotoActual: ImageView
    //var fotoActual: ImageView? = null
    lateinit var lugarActual: Lugar
    //var lugarActual: Lugar? = null  ////////
    lateinit var galeriaLauncher: ActivityResultLauncher<Intent>
    // lateinit var uriUltimaFoto: Uri  /////////
    var uriUltimaFoto: Uri? = null  /////////
    lateinit var tomarFotoLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("WrongConstant")
    private fun setGaleriaLauncher() {
        galeriaLauncher =
            (actividad as AppCompatActivity).registerForActivityResult(StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && lugarActual != null
                    && fotoActual != null //&& result.data != null
                ) {
                    val uri = result.data!!.data!!
                    actividad.getContentResolver().takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    ponerFoto(lugarActual, result.data!!.dataString, fotoActual)   //////
                } else {
                    Toast.makeText(actividad, "Foto no cargada", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun setTomarFotoLauncher() {
        tomarFotoLauncher =
            (actividad as AppCompatActivity).registerForActivityResult(
                StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && lugarActual != null
                    && fotoActual != null && uriUltimaFoto != null
                ) {
                    ponerFoto(lugarActual, uriUltimaFoto.toString(), fotoActual)  //////////////
                } else {
/*                  var s = lugarActual.toString();
                    s = fotoActual.toString()
                    s = uriUltimaFoto.toString()*/

                    Toast.makeText(
                        actividad, "Error al tomar foto",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun ponerDeGaleria(lugar: Lugar, foto: ImageView) {
        lugarActual = lugar
        fotoActual = foto
        val intent = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        galeriaLauncher.launch(intent)
    }

    fun tomarFoto(lugar: Lugar, foto: ImageView) {
        lugarActual = lugar
        fotoActual = foto
        try {
            val file = File.createTempFile(
                "img_" + System.currentTimeMillis() / 1000, ".jpg",
                actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
            uriUltimaFoto = if (Build.VERSION.SDK_INT >= 24)
                FileProvider.getUriForFile(
                    actividad, "es.upv.jtomas.mislugareskotlin.fileProvider", file
                )
            else Uri.fromFile(file)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto)
            tomarFotoLauncher.launch(intent)
        } catch (ex: IOException) {
            Toast.makeText(
                actividad, "Error al crear fichero de imagen",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun ponerFoto(lugar: Lugar, uri: String?, imageView: ImageView) {
        lugar.foto = uri.toString()
        visualizarFoto(lugar, imageView)
    }

    fun visualizarFoto(lugar: Lugar, imageView: ImageView) {
        if (lugar.foto != null && !lugar.foto.isEmpty()) {
            imageView.setImageURI(Uri.parse(lugar.foto));
        } else {
            imageView.setImageBitmap(null);
        }
    }

}
