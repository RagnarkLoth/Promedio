package com.example.myapplication

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.data.ConexionBaseDeDatos
import java.lang.Exception
import java.util.concurrent.RecursiveTask

class MainActivity : AppCompatActivity() {

    lateinit var  conexionBaseDeDatos: ConexionBaseDeDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val elementosEstudiante = arrayOf(findViewById<EditText>(R.id.idNombre), findViewById<EditText>(R.id.idDocumento), findViewById<EditText>(R.id.idDireccion), findViewById<EditText>(R.id.idEdad), findViewById<EditText>(R.id.idTelefono))

        val boton: Button = findViewById<Button>(R.id.idBottonEnviar)

        val botonEstadisiticas: Button = findViewById<Button>(R.id.idButtonEstadisticas)

        val intentNotas = Intent(this, NotasActivity::class.java)

        val intentEstadisticas = Intent(this, EstadisticasActivity::class.java)

        val intentAyuda = Intent(this, AyudaActivity::class.java)

        val botonAyuda: Button = findViewById(R.id.idButtonAyuda)

        conexionBaseDeDatos = ConexionBaseDeDatos(this)

        if(conexionBaseDeDatos.tablaClasificacionConteo() == 0){

            conexionBaseDeDatos.insertarCategorias()

        }

        boton.setOnClickListener {

            var estudiante:Estudiante = Estudiante(0, elementosEstudiante[0].text.toString(), elementosEstudiante[1].text.toString(), elementosEstudiante[2].text.toString(), elementosEstudiante[3].text.toString(), elementosEstudiante[4].text.toString())

            var errores:Int = 0

            elementosEstudiante.forEach {
                elemento ->
                    if(!modificacionMain(elemento, estudiante.verificarInformacionEstudiante(elemento))){
                        errores++
                    }
            }

            if(errores == 0 ){
                conexionBaseDeDatos.insertarEstudiantes(estudiante)

                estudiante = conexionBaseDeDatos.getEstudiantes()

                intentNotas.putExtra("id", estudiante.id.toString())

                startActivity(intentNotas)
            }
        }

        botonEstadisiticas.setOnClickListener {

            startActivity(intentEstadisticas)
        }

        botonAyuda.setOnClickListener {

            startActivity(intentAyuda)

        }
    }

    fun modificacionMain(campo:EditText, validacion:Boolean):Boolean{
        val titulo:TextView = when(campo){
            findViewById<EditText>(R.id.idNombre)-> {
                findViewById(R.id.idTextNombre)
            }
            findViewById<EditText>(R.id.idDocumento) -> {
                findViewById(R.id.idTextDocumento)
            }
            findViewById<EditText>(R.id.idTelefono) -> {
                findViewById(R.id.idTextTelefono)
            }
            findViewById<EditText>(R.id.idDireccion) -> {
                findViewById(R.id.idTextDireccion)
            }
            findViewById<EditText>(R.id.idEdad) -> {
                findViewById(R.id.idTextEdad)
            }
            else -> findViewById(R.id.idTextNombre)
        }
        if(validacion){
            titulo.setTextColor(Color.RED)
            campo.background.mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
            Toast.makeText(this, "El campo " + titulo.text + " esta vacio", Toast.LENGTH_SHORT).show()
            return false
        }else{
            titulo.setTextColor(Color.rgb(223,230,213))
            campo.background.mutate().setColorFilter(Color.rgb(223,230,213), PorterDuff.Mode.SRC_ATOP)
            return verificarTipoDato(campo, titulo)
        }
    }

    fun verificarTipoDato(campo: EditText, titulo:TextView):Boolean{

        try{

            if(titulo.text.equals("Documento")){

                Integer.parseInt(campo.text.toString())

            }else if(titulo.text.equals("Edad")){

                Integer.parseInt(campo.text.toString())

            }else if(titulo.text.equals("Telefono")){

                campo.text.toString().toLong()

            }


            return true

        }catch (ex:Exception){

            Toast.makeText(this, "El campo " + titulo.text + " solo acepta numeros ", Toast.LENGTH_LONG).show()
            titulo.setTextColor(Color.RED)
            campo.background.mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
            return false
        }

    }


}

