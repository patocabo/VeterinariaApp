package com.example.veterinaria.viewmodel

import android.content.Context
import android.icu.util.Calendar
import android.widget.CalendarView
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veterinaria.dao.DbHelper
import com.example.veterinaria.model.Turno
import com.example.veterinaria.model.Veterinario

class TurnoViewModel : ViewModel() {

    val veterinarios = MutableLiveData<ArrayList<Veterinario>>()

    fun saveTurno(
        tipo: String,
        nombre: String,
        raza: String,
        edad: Int,
        razon: String,
        turno: String,
        context: Context,
        vet: Veterinario
    ) {
        val nuevoTurno = Turno(tipo, nombre, raza, edad, razon, turno)
        val db = DbHelper(context, null)
        db.saveTurno(nuevoTurno, vet)
        getVetAvailables(turno, context)
    }

    fun getVetAvailables(date: String, context: Context) {
        val db: DbHelper = DbHelper(context, null)
        if (db.countTurnoByDate(date) < 20) {
            veterinarios.postValue(db.getVetsForDate(date, context))
        } else veterinarios.postValue(ArrayList<Veterinario>())
    }

}