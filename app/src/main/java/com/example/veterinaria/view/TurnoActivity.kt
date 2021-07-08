package com.example.veterinaria.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.veterinaria.R
import com.example.veterinaria.model.Veterinario
import com.example.veterinaria.viewmodel.TurnoViewModel
import java.lang.String.format
import java.text.DateFormat

class TurnoActivity : AppCompatActivity() {

    lateinit var datePicker: EditText
    lateinit var nombre: EditText
    lateinit var raza: EditText
    lateinit var edad: EditText
    lateinit var causas: EditText
    lateinit var clase: Spinner
    lateinit var picker: DatePickerDialog
    lateinit var cldr: Calendar
    lateinit var save: Button
    lateinit var turnoVM: TurnoViewModel
    lateinit var vetPicker: Spinner
    private lateinit var inicio: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turno)
        initialize()
        setupDatePicker()
        turnoVM = ViewModelProvider(this).get(TurnoViewModel::class.java)
        setupOnClickListeners()
        setupObservers()


    }

    private fun setupOnClickListeners() {
        save.setOnClickListener {
            if (checkAllFields()) {
                turnoVM.saveTurno(
                    clase.selectedItem.toString(),
                    nombre.text.toString(),
                    raza.text.toString(),
                    edad.text.toString().toInt(),
                    causas.text.toString(),
                    datePicker.text.toString(),
                    this,
                    vetPicker.selectedItem as Veterinario
                )
                Toast.makeText(
                    this,
                    "Se ha registrado el turno de ${nombre.text}!",
                    Toast.LENGTH_SHORT
                )
                    .show()
                nombre.setText("")
                raza.setText("")
                raza.setText("")
                edad.setText("")
                causas.setText("")
                datePicker.setText("")
            }

        }
        inicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
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
                    turnoVM.getVetAvailables("$dayOfMonth/${month + 1}/$year", this)

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
        nombre = findViewById(R.id.e_petname)
        raza = findViewById(R.id.e_petrace)
        edad = findViewById(R.id.e_age)
        causas = findViewById(R.id.e_reason)
        clase = findViewById(R.id.s_petclass)
        clase.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Clase,
            android.R.layout.simple_spinner_item
        )
        save = findViewById(R.id.b_save)
        vetPicker = findViewById(R.id.s_vet)
        inicio = findViewById(R.id.bt_back2)

    }

    private fun setupObservers() {
        turnoVM.getVetAvailables("asd", this)
        turnoVM.veterinarios.observe(this, {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
            vetPicker.adapter = adapter
        })
    }

    private fun checkAllFields(): Boolean {
        var pasa = true
        if (nombre.length() == 0) {
            nombre.error = "Este campo es requerido"
            pasa = false
        }
        if (raza.length() == 0) {
            raza.error = "Este campo es requerido"
            pasa = false

        }
        if (edad.length() == 0) {
            edad.error = "Este campo es requerido"
            pasa = false

        }
        if (causas.length() == 0) {
            causas.error = "Este campo es requerido"
            pasa = false

        }
        if (datePicker.length() == 0) {
            nombre.error = "Este campo es requerido"
            pasa = false
        }
        return pasa
    }

}