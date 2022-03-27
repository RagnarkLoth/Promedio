package com.example.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AyudaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)

        val botonVolver:Button = findViewById(R.id.idButtonVolverA)

        val intentMain = Intent(this, MainActivity::class.java)

        botonVolver.setOnClickListener {

            startActivity(intentMain)

        }

    }
}