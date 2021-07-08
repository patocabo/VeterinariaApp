package com.example.veterinaria.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.veterinaria.R
import com.example.veterinaria.viewmodel.DiagnosticarViewModel

class DiagnosticarActivity : AppCompatActivity() {

    lateinit var nombre: TextView
    lateinit var raza: TextView
    lateinit var edad: TextView
    lateinit var clase: TextView
    lateinit var causa: TextView
    lateinit var diagnosticar: Button
    lateinit var setMed: TextView
    lateinit var setDiag: TextView
    lateinit var diagnosticarVM: DiagnosticarViewModel
    lateinit var diagnostico: EditText
    lateinit var medicacion: EditText
    private lateinit var inicio: Button
    private lateinit var volver: Button


    var turno_id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosticar)
        initialize()
        mapElements()
        diagnosticarVM = ViewModelProvider(this).get(DiagnosticarViewModel::class.java)
        setupOnClickListeners()
        setupObservers()
        diagnosticarVM.getDiagnostic(turno_id, this)
    }

    private fun initialize() {
        nombre = findViewById(R.id.diag_nombre)
        raza = findViewById(R.id.diag_raza)
        edad = findViewById(R.id.diag_edad)
        clase = findViewById(R.id.diag_clase)
        causa = findViewById(R.id.diag_causa)
        diagnosticar = findViewById(R.id.diag_b_save)
        setDiag = findViewById(R.id.diag_t_diagnostico)
        setMed = findViewById(R.id.diag_t_med)
        medicacion = findViewById(R.id.diag_med)
        diagnostico = findViewById(R.id.diag_diagnostico)
        inicio = findViewById(R.id.bt_back)
        volver = findViewById(R.id.bt_back4)


    }

    private fun mapElements() {
        nombre.text = "Nombre: " + intent.getStringExtra("nombre")
        raza.text = "Raza: " + intent.getStringExtra("raza")
        edad.text = "Edad: " + intent.getStringExtra("edad")
        clase.text = "Clase: " + intent.getStringExtra("clase")
        causa.text = "Causa: " + intent.getStringExtra("causa")
        turno_id = intent.getStringExtra("turno_id")!!.toInt()

        if (intent.getStringExtra("diagnosticado")!!.toInt() == 0) {
            setDiag.text = "Ingrese el diagnostico:"
            setMed.text = "Ingrese la medicacion:"
            medicacion.isEnabled = true
            diagnostico.isEnabled = true
        } else {
            setDiag.text = "Diagnostico: "
            setMed.text = "Medicacion recetada: "
            diagnosticar.visibility = View.INVISIBLE
        }
    }

    private fun setupOnClickListeners() {
        diagnosticar.setOnClickListener {
            diagnosticarVM.insertDiagnostico(
                medicacion.text.toString(),
                diagnostico.text.toString(),
                turno_id,
                this
            )
            Toast.makeText(this, "Diagnostico guardado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, VeterinarioActivity::class.java))
        }
        inicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }
        volver.setOnClickListener {
            val intent = Intent(this, VeterinarioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        diagnosticarVM.diagnostico.observe(this, {
            diagnostico.setText(it.diagnostico)
            diagnostico.isEnabled = false
            medicacion.setText(it.medicacion)
            medicacion.isEnabled = false
        })
    }
}