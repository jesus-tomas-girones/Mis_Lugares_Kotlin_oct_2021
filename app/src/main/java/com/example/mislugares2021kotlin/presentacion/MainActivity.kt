package com.example.mislugares2021kotlin.presentacion

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mislugares2021kotlin.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mislugares2021kotlin.Aplicacion
import com.example.mislugares2021kotlin.R
import com.example.mislugares2021kotlin.casos_uso.CasosUsoActividades
import com.example.mislugares2021kotlin.casos_uso.CasosUsoLocalizacion
import com.example.mislugares2021kotlin.casos_uso.CasosUsoLugar
import com.example.mislugares2021kotlin.datos.RepositorioLugares
import java.lang.Integer.parseInt


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //val lugares by lazy { (application as Aplicacion).lugares }
    //val usoLugar by lazy { CasosUsoLugar(this, lugares) }
    //val lugares = (application as Aplicacion).lugares
    //val usoLugar = CasosUsoLugar(this, lugares)

    lateinit var lugares: RepositorioLugares
    lateinit var usoLugar: CasosUsoLugar

    val usoActividades by lazy { CasosUsoActividades(this) }
    //val adaptador by lazy { (application as Aplicacion).adaptador }
    lateinit var adaptador: AdaptadorLugares
    val SOLICITUD_PERMISO_LOCALIZACION = 1
    val usoLocalizacion by lazy {
        CasosUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lugares = (application as Aplicacion).lugares
        usoLugar = CasosUsoLugar(this, lugares)

        adaptador = (application as Aplicacion).adaptador
        setSupportActionBar(/*findViewById(R.id.toolbar)*/binding.toolbar)
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        adaptador.onClick = {
            val pos: Int = binding.content.recyclerView.getChildAdapterPosition(it)
            usoLugar.mostrar(pos)
        }
        binding.content.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adaptador
        }
    }

    override fun onResume() {
        super.onResume()
        adaptador.notifyDataSetChanged()
        usoLocalizacion.activar()
    }

    override fun onPause() {
        super.onPause()
        usoLocalizacion.desactivar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> {
                usoActividades.lanzarPreferencias()
                true
            }
            R.id.acercaDe -> {
                usoActividades.lanzarAcercaDe()
                true
            }
            R.id.menu_buscar -> {
                lanzarVistaLugar()
                true
            }
            R.id.menu_mapa -> {
                startActivity(Intent(this, MapaActivity::class.java))
                true;
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults)
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
            && grantResults.size == 1
            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            usoLocalizacion.permisoConcedido()
    }

    fun lanzarVistaLugar(view: View? = null) {
        val entrada = EditText(this)
        entrada.setText("0")
        AlertDialog.Builder(this)
            .setTitle("Selección de lugar")
            .setMessage("indica su id:")
            .setView(entrada)
            .setPositiveButton("Ok")  { dialog, whichButton ->
                val id = parseInt(entrada.text.toString())
                usoLugar.mostrar(id);
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}