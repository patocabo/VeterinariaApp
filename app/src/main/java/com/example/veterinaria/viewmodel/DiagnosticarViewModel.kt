package com.example.veterinaria.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veterinaria.dao.DbHelper
import com.example.veterinaria.model.Diagnostico

class DiagnosticarViewModel : ViewModel() {

    val diagnostico = MutableLiveData<Diagnostico>()

    fun insertDiagnostico(diag: String, medicacion: String, turno_id: Int, context: Context) {
        val db = DbHelper(context, null)
        db.insertDiagnostic(Diagnostico(diag, medicacion), turno_id)
    }

    fun getDiagnostic(turno_id: Int, context: Context) {
        val db = DbHelper(context, null)
        if (!(db.getDiagnosticoById(turno_id).equals(Diagnostico("", ""))))
            diagnostico.postValue(db.getDiagnosticoById(turno_id))

    }
}