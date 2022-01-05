package com.example.mislugares2021kotlin.presentacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mislugares2021kotlin.Aplicacion
import com.example.mislugares2021kotlin.modelo.Lugar
import com.example.mislugares2021kotlin.R
import com.example.mislugares2021kotlin.datos.RepositorioLugares
import com.example.mislugares2021kotlin.modelo.TipoLugar
import com.example.mislugares2021kotlin.databinding.ElementoListaBinding
import com.example.mislugares2021kotlin.modelo.GeoPunto

class AdaptadorLugares(
    val lugares: RepositorioLugares
    /*,val onClick: (View) -> Unit*/
) :

    RecyclerView.Adapter<AdaptadorLugares.ViewHolder>() {

    lateinit var onClick: (View) -> Unit

    class ViewHolder(val view: ElementoListaBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun personaliza(lugar: Lugar, onClick: (View) -> Unit) =
            with(view) {
                nombre.text = lugar.nombre
                direccion.text = lugar.direccion
                foto.setImageResource(
                    when (lugar.tipoLugar) {
                        TipoLugar.RESTAURANTE -> R.drawable.restaurante
                        TipoLugar.BAR -> R.drawable.bar
                        TipoLugar.COPAS -> R.drawable.copas
                        TipoLugar.ESPECTACULO -> R.drawable.espectaculos
                        TipoLugar.HOTEL -> R.drawable.hotel
                        TipoLugar.COMPRAS -> R.drawable.compras
                        TipoLugar.EDUCACION -> R.drawable.educacion
                        TipoLugar.DEPORTE -> R.drawable.deporte
                        TipoLugar.NATURALEZA -> R.drawable.naturaleza
                        TipoLugar.GASOLINERA -> R.drawable.gasolinera
                        TipoLugar.OTROS -> R.drawable.otros
                    }
                )
                foto.setScaleType(ImageView.ScaleType.FIT_END)
                valoracion.rating = lugar.valoracion
                root.setOnClickListener { onClick(itemView) } /////
                val pos = (itemView.context.applicationContext as Aplicacion).posicionActual
                if (pos.equals(GeoPunto.SIN_POSICION) || lugar.posicion.equals(GeoPunto.SIN_POSICION)) {
                    distancia.text = "... Km"
                } else {
                    val d = pos.distancia(lugar.posicion).toInt()
                    distancia.text = if (d < 2000) "$d m"
                    else "${(d / 1000)} Km"

                }


            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
//        val v = LayoutInflater.from(parent.context)
//            .inflate(R.layout.elemento_lista, parent, false)
        val binding =
            ElementoListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding) //ElementoListaBinding.bind(v))  ///?????
    }

    override fun onBindViewHolder(holder: ViewHolder, posicion: Int) {
        val lugar = lugares.elemento(posicion)
        holder.personaliza(lugar, onClick)
    }

    override fun getItemCount() = lugares.tamaño()
}
