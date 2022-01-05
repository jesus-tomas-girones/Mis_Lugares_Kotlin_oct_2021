package com.example.mislugares2021kotlin.datos

import com.example.mislugares2021kotlin.modelo.Lugar

class LugaresLista : RepositorioLugares {
    val listaLugares= mutableListOf<Lugar>()

    override fun elemento(id: Int): Lugar {
        return listaLugares[id]
    }

    override fun añade(lugar: Lugar) {
        listaLugares.add(lugar)
    }

    override fun nuevo(): Int {
        val lugar = Lugar("Nuevo lugar")
        listaLugares.add(lugar)
        return listaLugares.size - 1
    }

    override fun borrar(id: Int) {
        listaLugares.removeAt(id)
    }

    override fun tamaño(): Int {
        return listaLugares.size
    }

    override fun actualiza(id: Int, lugar: Lugar) {
        listaLugares[id] = lugar
    }
}
