package com.example.mislugares2021kotlin

import android.app.Application
import android.widget.Toast
import com.example.mislugares2021kotlin.datos.LugaresLista
import com.example.mislugares2021kotlin.modelo.GeoPunto
import com.example.mislugares2021kotlin.presentacion.AdaptadorLugares


class Aplicacion : Application() {

    var lugares = LugaresLista()
    val adaptador = AdaptadorLugares(lugares)
//    val posicionActual = GeoPunto(0.0,0.0)//.SIN_POSICION
    val posicionActual = GeoPunto.SIN_POSICION.copy()
    /*{
        //val pos: Int = recyclerView.getChildAdapterPosition(it)
        Toast.makeText(this, "pulsado ", Toast.LENGTH_LONG).show()
    }*/

    override fun onCreate() {
        super.onCreate()
        lugares.añadeEjemplos()
    }
}
