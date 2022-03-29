package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import com.example.myapplication.data.ConexionBaseDeDatos
import java.util.concurrent.TimeUnit

class TablaNotas : AppCompatActivity() {

    var tableInformacion:TableLayout? =  null

    var conexionBaseDeDatos: ConexionBaseDeDatos = ConexionBaseDeDatos(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabla_notas)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        val sharedPrefEdit = sharedPref.edit()

        var mensajePromedio:TextView = findViewById(R.id.idTextMensaje)

        var contadorText:TextView = findViewById(R.id.idTextContador)

        var bundle: Bundle? = this.intent.extras


        var objetoEstudiante:Estudiante = conexionBaseDeDatos.getEstudiantesId(bundle?.getInt("id")!!)

        tableInformacion = findViewById(R.id.idTableInformacion)

        val intent = Intent(this, MainActivity::class.java)

        var objetoMateria: List<Materias> = conexionBaseDeDatos.getEstudiantesMaterias(objetoEstudiante.id)

        cargarTabla(objetoMateria, objetoEstudiante, mensajePromedio, objetoEstudiante)

        contador(intent, contadorText, sharedPrefEdit)

    }

    fun contador(intent: Intent, mensajeContador:TextView, sharedPreferences: SharedPreferences.Editor){

        val tiempo = object:CountDownTimer(30000, 1000){
            override fun onTick(segundos: Long) {

                mensajeContador.setText((segundos/1000).toString())

            }

            override fun onFinish() {

                sharedPreferences.clear().apply()
                startActivity(intent)

            }

        }

        tiempo.start()

    }

    fun cargarTabla(objetoMateria: List<Materias>, estudiante: Estudiante, mensajePromedio:TextView, objetoEstudiante: Estudiante) {

        var notaPromedio:Double = 0.0

        objetoMateria.forEach { materia ->

            val registro =
                LayoutInflater.from(this).inflate(R.layout.registros_table_layout, null, false)

            var textViewNombreMateria =
                registro.findViewById<View>(R.id.idTextNombreMaterias) as TextView
            var textViewNotaMateria =
                registro.findViewById<View>(R.id.idTextNotaMaterias) as TextView

            textViewNombreMateria.setText(materia.nombreMateria)
            textViewNotaMateria.setText(materia.notaMateria.toString())

            notaPromedio+=materia.notaMateria

            tableInformacion?.addView(registro)
        }

        val pie = LayoutInflater.from(this).inflate(R.layout.registros_table_layout, null, false)

        var textViewNombrePromedio =
            pie.findViewById<View>(R.id.idTextNombreMaterias) as TextView
        var textViewNotaPromedio =
            pie.findViewById<View>(R.id.idTextNotaMaterias) as TextView

        textViewNombrePromedio.setText("Promedio")
        textViewNotaPromedio.setText((notaPromedio/5).toString().substring(0,3))

        if((notaPromedio/5) > 3.5){

            mensajePromedio.setText("El estudinate ${estudiante.nombre} ha ganado el periodo")

            val categoria: Categoria = conexionBaseDeDatos.buscarCategoriasNombre("gano")

            conexionBaseDeDatos.actualizarRegistroEstudiante(objetoEstudiante, categoria)

        }else if((notaPromedio/5) > 2.5 && (notaPromedio/5) <= 3.5){

            mensajePromedio.setText("El estudiante ${estudiante.nombre} puede recuperar el periodo")

            val categoria: Categoria = conexionBaseDeDatos.buscarCategoriasNombre("recuperacion")

            conexionBaseDeDatos.actualizarRegistroEstudiante(objetoEstudiante, categoria)

        } else if((notaPromedio/5) < 2.5){

            mensajePromedio.setText("El estudiante ${estudiante.nombre} ha perdido el periodo sin opcion a recuperar")

            val categoria: Categoria = conexionBaseDeDatos.buscarCategoriasNombre("perdio")

            conexionBaseDeDatos.actualizarRegistroEstudiante(objetoEstudiante, categoria)

        }

        tableInformacion?.addView(pie)
    }
}