package com.example.veterinaria.viewmodel

import android.content.Context
import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veterinaria.dao.DbHelper
import com.example.veterinaria.model.Turno
import com.example.veterinaria.model.Veterinario

class VeterinarioViewModel : ViewModel() {

    val veterinarios = MutableLiveData<ArrayList<Veterinario>>()
    val turnos = MutableLiveData<ArrayMap<Turno, IntArray>>()

    fun getVet(date: String, context: Context) {
        val db: DbHelper = DbHelper(context, null)
        veterinarios.postValue(db.getAllVet())
    }

    fun getTurnos(date: String, vet: Veterinario, context: Context) {
        val db: DbHelper = DbHelper(context, null)
        turnos.postValue(db.getTurnosByDateAndVetId(date, vet.id))
    }
}