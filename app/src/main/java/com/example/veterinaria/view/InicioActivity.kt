package com.example.veterinaria.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.veterinaria.R

class InicioActivity : AppCompatActivity() {

    lateinit var user: Button
    lateinit var veterinary: Button
    lateinit var dueno: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        initialization()
        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        user.setOnClickListener {
            startActivity(Intent(this, TurnoActivity::class.java))
        }
        veterinary.setOnClickListener {
            startActivity(Intent(this, VeterinarioActivity::class.java))
        }
        dueno.setOnClickListener {
            startActivity(Intent(this, DuenoActivity::class.java))
        }
    }

    private fun initialization() {
        user = findViewById(R.id.b_i_user)
        veterinary = findViewById(R.id.b_i_vet)
        dueno = findViewById(R.id.b_i_adm)
    }


}