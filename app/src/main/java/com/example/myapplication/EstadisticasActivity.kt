package com.example.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.data.ConexionBaseDeDatos


class EstadisticasActivity : AppCompatActivity() {

    lateinit var  conexionBaseDeDatos: ConexionBaseDeDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        val intent = Intent(this, MainActivity::class.java)

        var contadorProcesador:TextView = findViewById(R.id.idTextContadorP)

        var contadorPerdieron:TextView = findViewById(R.id.idTextContadorPe)

        var contadorRecuperando:TextView = findViewById(R.id.idTextContadorR)

        var contadorGanaron: TextView = findViewById(R.id.idTextContadorG)

        var botonVolver:Button = findViewById(R.id.idButtonVolver)

        conexionBaseDeDatos = ConexionBaseDeDatos(this)

        if(conexionBaseDeDatos.tablaEstudiantesConteo() == 0){

            contadorGanaron.setText("0")
            contadorPerdieron.setText("0")
            contadorProcesador.setText("0")
            contadorRecuperando.setText("0")

        }else{

            var categoria:Categoria = conexionBaseDeDatos.buscarCategoriasNombre("gano")
            contadorGanaron.setText(conexionBaseDeDatos.getEstudianteCategoriaEstudiante(categoria).toString())
            categoria = conexionBaseDeDatos.buscarCategoriasNombre("perdio")
            contadorPerdieron.setText(conexionBaseDeDatos.getEstudianteCategoriaEstudiante(categoria).toString())
            contadorProcesador.setText(conexionBaseDeDatos.tablaEstudiantesConteo().toString())
            categoria = conexionBaseDeDatos.buscarCategoriasNombre("recuperacion")
            contadorRecuperando.setText(conexionBaseDeDatos.getEstudianteCategoriaEstudiante(categoria).toString())

        }

        botonVolver.setOnClickListener {

            startActivity(intent)

        }

    }
}