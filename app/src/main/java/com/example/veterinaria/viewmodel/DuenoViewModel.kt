package com.example.veterinaria.viewmodel

import android.content.Context
import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veterinaria.dao.DbHelper
import com.example.veterinaria.model.Veterinario

class DuenoViewModel : ViewModel() {

    val registros = MutableLiveData<ArrayMap<Veterinario, IntArray>>()

    fun getStats(date: String, context: Context) {
        val db = DbHelper(context, null)
        registros.postValue(db.getRegistersByDate(date))
    }


}