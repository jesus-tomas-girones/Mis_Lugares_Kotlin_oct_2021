package com.example.mislugares2021kotlin.presentacion

import android.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mislugares2021kotlin.Aplicacion
import com.example.mislugares2021kotlin.casos_uso.CasosUsoLugar
import com.example.mislugares2021kotlin.databinding.EdicionLugarBinding
import com.example.mislugares2021kotlin.datos.RepositorioLugares
import com.example.mislugares2021kotlin.modelo.Lugar
import com.example.mislugares2021kotlin.modelo.TipoLugar

class EdicionLugarActivity: AppCompatActivity() {

    //val lugares by lazy { (application as Aplicacion).lugares }
    //val usoLugar by lazy { CasosUsoLugar(this, lugares) }
    lateinit var lugares: RepositorioLugares
    lateinit var usoLugar: CasosUsoLugar

    var pos = 0
    lateinit var lugar: Lugar
    private lateinit var binding: EdicionLugarBinding

    lateinit var vistaPrincipal: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EdicionLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pos = intent.extras?.getInt("pos", 0) ?: 0
        lugares = (application as Aplicacion).lugares
        usoLugar = CasosUsoLugar(this, lugares)
        actualizaVistas()
    }

    fun actualizaVistas(){
        lugar = lugares.elemento(pos)
        binding.nombre.setText( lugar.nombre )
        binding.direccion.setText(  lugar.direccion)
        binding.telefono.setText(  Integer.toString(lugar.telefono))
        binding.url.setText(  lugar.url)
        binding.comentario.setText(  lugar.comentarios)
        val adaptador = ArrayAdapter<String>(this,
            R.layout.simple_spinner_item,
            lugar.tipoLugar.getNombres()
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tipo.adapter = adaptador
        binding.tipo.setSelection(lugar.tipoLugar.ordinal)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.example.mislugares2021kotlin.R.menu.edicion_lugar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            com.example.mislugares2021kotlin.R.id.accion_cancelar -> {
                setResult(RESULT_CANCELED)
                finish()
                return true
            }
            com.example.mislugares2021kotlin.R.id.accion_guardar -> {
                val nuevoLugar = Lugar(binding.nombre.text.toString(),
                    binding.direccion.text.toString(), lugar.posicion,
                    TipoLugar.values()[binding.tipo.selectedItemPosition],
                    lugar.foto, Integer.parseInt(binding.telefono.text.toString()),
                    binding.url.text.toString(), binding.comentario.text.toString(),
                    lugar.fecha, lugar.valoracion)
                usoLugar.guardar(pos, nuevoLugar)
                setResult(RESULT_OK)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}