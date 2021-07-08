package com.example.veterinaria.viewmodel.recyclerview

import android.content.Intent
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veterinaria.R
import com.example.veterinaria.model.Turno
import com.example.veterinaria.view.DiagnosticarActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception


class TurnoAdapter(var lista: ArrayMap<Turno, IntArray>) :
    RecyclerView.Adapter<TurnoAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var nombre: TextView
        var clase: TextView
        var edad: TextView
        var raza: TextView
        var diagnosticado: CheckBox

        var diagnosticar: FloatingActionButton

        init {
            nombre = view.findViewById(R.id.det_nombre2)
            clase = view.findViewById(R.id.det_clase)
            raza = view.findViewById(R.id.det_raza)
            edad = view.findViewById(R.id.det_edad)
            diagnosticar = view.findViewById(R.id.floating_action_button)
            diagnosticado = view.findViewById(R.id.det_diagnosticado)


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.diagnostico_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.nombre.text = lista.keyAt(position).nombre
        holder.raza.text = lista.keyAt(position).raza
        holder.edad.text = lista.keyAt(position).edad.toString()
        holder.clase.text = lista.keyAt(position).tipo

        holder.diagnosticado.isChecked = lista.valueAt(position)[0] == 1


        holder.diagnosticar.setOnClickListener {
            try {
                val intent = Intent(it.context, DiagnosticarActivity::class.java)
                intent.putExtra("nombre", lista.keyAt(position).nombre)
                intent.putExtra("raza", lista.keyAt(position).raza)
                intent.putExtra("edad", lista.keyAt(position).edad.toString())
                intent.putExtra("clase", lista.keyAt(position).tipo)
                intent.putExtra("causa", lista.keyAt(position).razon)
                intent.putExtra("diagnosticado", lista.valueAt(position)[0].toString())
                intent.putExtra("turno_id", lista.valueAt(position)[1].toString())

                it.context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}