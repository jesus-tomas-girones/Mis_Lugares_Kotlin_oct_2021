package com.example.mislugares2021kotlin.presentacion

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mislugares2021kotlin.Aplicacion
import com.example.mislugares2021kotlin.R
import com.example.mislugares2021kotlin.casos_uso.CasosUsoLugar
import com.example.mislugares2021kotlin.databinding.ActivityMainBinding
import com.example.mislugares2021kotlin.databinding.ActivityVistaLugarBinding
import com.example.mislugares2021kotlin.databinding.VistaLugarBinding
import com.example.mislugares2021kotlin.datos.RepositorioLugares
import com.example.mislugares2021kotlin.modelo.Lugar
import java.text.DateFormat
import java.util.*

class VistaLugarActivity : AppCompatActivity() {
//    val lugares by lazy { (application as Aplicacion).lugares }
//    val usoLugar by lazy { CasosUsoLugar(this, lugares) }
    lateinit var lugares: RepositorioLugares
    lateinit var usoLugar: CasosUsoLugar
    var pos = 0
    lateinit var lugar: Lugar
    private lateinit var binding: VistaLugarBinding

    var edicionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if (result.resultCode == Activity.RESULT_OK) {
        actualizaVistas();
    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VistaLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pos = intent.extras?.getInt("pos", 0) ?: 0
//////        lugar = lugares.elemento(pos)
        lugares = (application as Aplicacion).lugares
        usoLugar = CasosUsoLugar(this, lugares)
        actualizaVistas()
    }

    fun actualizaVistas(){
        lugar = lugares.elemento(pos)  /////
        binding.nombre.text = lugar.nombre
        binding.logoTipo.setImageResource(lugar.tipoLugar.recurso)
        binding.tipo.text = lugar.tipoLugar.texto
        binding.direccion.text = lugar.direccion
        if (lugar.telefono == 0) {
            binding.barraTelefono.setVisibility(View.GONE)
        } else {
            binding.barraTelefono.setVisibility(View.VISIBLE)
            binding.telefono.text = Integer.toString(lugar.telefono)
        }
        binding.url.text = lugar.url
        binding.comentario.text = lugar.comentarios
        binding.fecha.text = DateFormat.getDateInstance().format(
            Date(lugar.fecha))
        binding.hora.text = DateFormat.getTimeInstance().format(
            Date(lugar.fecha)
        )
        binding.valoracion.rating = lugar.valoracion
        binding.valoracion.setOnRatingBarChangeListener {
                ratingBar, valor, fromUser -> lugar.valoracion = valor
        }
        usoLugar.visualizarFoto(lugar, binding.foto)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.vista_lugar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.accion_compartir -> {
                usoLugar.compartir(lugar)
                return true
            }
            R.id.accion_llegar -> {
                usoLugar.verMapa(lugar)
                return true
            }
            R.id.accion_editar -> {
                usoLugar.editar(pos, edicionLauncher)
                return true
            }
            R.id.accion_borrar -> {
                usoLugar.borrar(pos)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun eliminarFoto(view: View) = usoLugar.ponerFoto(lugar,"", binding.foto)

    fun verMapa(view: View) = usoLugar.verMapa(lugar)

    fun llamarTelefono(view: View) = usoLugar.llamarTelefono(lugar)

    fun verPgWeb(view: View) = usoLugar.verPgWeb(lugar)

    fun galeria(view: View) = usoLugar.ponerDeGaleria(lugar, binding.foto)

    fun tomarFoto(view: View) = usoLugar.tomarFoto (lugar, binding.foto)


}
