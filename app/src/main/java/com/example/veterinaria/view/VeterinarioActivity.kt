package com.example.veterinaria.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.widget.LinearLayout.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veterinaria.R
import com.example.veterinaria.model.Veterinario
import com.example.veterinaria.viewmodel.VeterinarioViewModel
import com.example.veterinaria.viewmodel.recyclerview.TurnoAdapter

class VeterinarioActivity : AppCompatActivity() {

    lateinit var datePicker: EditText
    lateinit var vetPicker: Spinner
    lateinit var cldr: Calendar
    lateinit var picker: DatePickerDialog
    lateinit var veteVM: VeterinarioViewModel
    lateinit var rvTurnos: RecyclerView
    lateinit var encabezado: LinearLayout
    private lateinit var inicio: Button


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinario)
        initialize()
        veteVM = ViewModelProvider(this).get(VeterinarioViewModel::class.java)
        setupDatePicker()
        setupObservers()
        setupOnClickListeners()

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
                    veteVM.getTurnos(
                        "" + dayOfMonth + "/" + (month + 1) + "/" + year,
                        vetPicker.selectedItem as Veterinario,
                        this
                    )
                },
                year,
                month,
                day
            )
            picker.show()
            // picker.updateDate(2021,7,7)
        }
    }

    private fun initialize() {
        datePicker = findViewById(R.id.vet_datepicker)
        vetPicker = findViewById(R.id.s_vet)
        rvTurnos = findViewById(R.id.rv_turnos)
        encabezado = findViewById(R.id.turnos_encabezados)
        inicio = findViewById(R.id.bt_back3)

    }

    @SuppressLint("WrongConstant")
    private fun setupObservers() {
        veteVM.getVet("asd", this)
        veteVM.veterinarios.observe(this, {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
            vetPicker.prompt = "Seleccione su veterinario..."
            vetPicker.adapter = adapter
        })
        veteVM.turnos.observe(this, {
            rvTurnos.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

            rvTurnos.adapter = TurnoAdapter(it)
            encabezado.visibility = VISIBLE
        })

    }

    private fun setupOnClickListeners() {

        inicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }
    }


}