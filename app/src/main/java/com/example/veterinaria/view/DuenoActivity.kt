package com.example.veterinaria.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veterinaria.R
import com.example.veterinaria.viewmodel.DuenoViewModel
import com.example.veterinaria.viewmodel.TurnoViewModel
import com.example.veterinaria.viewmodel.recyclerview.RegistroAdapter
import com.example.veterinaria.viewmodel.recyclerview.TurnoAdapter

class DuenoActivity : AppCompatActivity() {

    lateinit var datePicker: EditText
    lateinit var inicio: Button
    lateinit var cldr: Calendar
    lateinit var picker: DatePickerDialog
    lateinit var duenoVM: DuenoViewModel
    lateinit var rv_data: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dueno)
        duenoVM = ViewModelProvider(this).get(DuenoViewModel::class.java)
        initialize()
        setupDatePicker()
        setupObservers()
        setupOnClickListeners()


    }

    private fun setupOnClickListeners() {
        inicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }
    }


    fun setupDatePicker() {
        cldr = Calendar.getInstance();
        datePicker.setOnClickListener {
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            picker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener() { _, year, month, dayOfMonth ->
                    datePicker.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year)
                    duenoVM.getStats("$dayOfMonth/${month + 1}/$year", this)
                },
                year,
                month,
                day
            )
            picker.show()
        }
    }

    private fun initialize() {
        datePicker = findViewById(R.id.e_datepicker)
        rv_data = findViewById(R.id.rv_registros)
        inicio = findViewById(R.id.bt_back5)

    }

    @SuppressLint("WrongConstant")
    private fun setupObservers() {
        duenoVM.registros.observe(this, {
            rv_data.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            rv_data.adapter = RegistroAdapter(it)
        })
    }

}