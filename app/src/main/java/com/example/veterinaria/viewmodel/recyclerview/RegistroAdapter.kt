package com.example.veterinaria.viewmodel.recyclerview

import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veterinaria.R
import com.example.veterinaria.model.Veterinario

class RegistroAdapter(var list: ArrayMap<Veterinario, IntArray>) :
    RecyclerView.Adapter<RegistroAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var data: TextView


        init {
            data = view.findViewById(R.id.reg_dat)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.register_layout, parent, false)
        return RegistroAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.data.text =
            list.keyAt(position).nombre + " recibe " + list.valueAt(position)[0] + ///TODO PODRIA AGREGAR EL DETALLE DE LOS QUE DIAGNOSTICO
                    " pacientes el dia seleccionado."

    }

    override fun getItemCount(): Int {
        return list.size
    }

}