package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.*
import com.example.myapplication.data.ConexionBaseDeDatos
import org.w3c.dom.Text

class NotasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        val campoNombreMateria:EditText = findViewById(R.id.idEditNombreMateria)

        val campoNotaMateria:EditText = findViewById(R.id.idEditNotaMateria)

        val botonProcesar:Button = findViewById(R.id.idButtonProcesar)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        val sharedPrefEdit = sharedPref.edit()

        var bundle: Bundle? = this.intent.extras

        var conexionBaseDeDatos:ConexionBaseDeDatos = ConexionBaseDeDatos(this)

        var titulo: TextView = findViewById(R.id.idTextTituloNotas)

        var documento: TextView = findViewById(R.id.idTextDocumentoN)

        var edad: TextView = findViewById(R.id.idTextEdadN)

        val spinnerMaterias:Spinner = findViewById(R.id.idSpinnerMaterias)

        var objetoEstudiante:Estudiante = conexionBaseDeDatos.getEstudiantesId(bundle?.getString("id")!!.toInt())

        var posicionElemento:String? = null

        edad.setText(objetoEstudiante.edad)

        documento.setText(objetoEstudiante.documento)

        titulo.setText(objetoEstudiante.nombre)

        val intent = Intent(this, TablaNotas::class.java)

        val listaOpciones = arrayOf("Materia Uno", "Materia Dos", "Materia Tres", "Materia Cuatro", "Materia Cinco")

        var elementos: ArrayAdapter<String> = ArrayAdapter(this, R.layout.textospinner, listaOpciones)

        spinnerMaterias?.adapter = elementos

        spinnerMaterias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                posicionElemento = spinnerMaterias.getItemAtPosition(position).toString()
                modificacionHashPref(spinnerMaterias.getItemAtPosition(position).toString(), campoNombreMateria, campoNotaMateria, sharedPrefEdit, sharedPref)
                modificacionCampos(spinnerMaterias.getItemAtPosition(position).toString(), campoNotaMateria, campoNombreMateria, sharedPref)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        botonProcesar.setOnClickListener {

            guardarEmergencia(posicionElemento!!, campoNombreMateria, campoNotaMateria, sharedPrefEdit)

            var errores: Int = 0

            val notasTest:Array<Array<String>> = arrayOf(
                arrayOf(sharedPref.getString("nombresMateriaUno", "")!!.toString() ,sharedPref.getString("keyNotaMateriaUno", "0.0")!!.toString(), "Materia Uno"),
                arrayOf(sharedPref.getString("nombresMateriaDos", "")!!.toString() ,sharedPref.getString("keyNotaMateriaDos", "0.0")!!.toString(), "Materia Dos"),
                arrayOf(sharedPref.getString("nombresMateriaTres", "")!!.toString() ,sharedPref.getString("keyNotaMateriaTres", "0.0")!!.toString(), "Materia Tres"),
                arrayOf(sharedPref.getString("nombresMateriaCuatro", "")!!.toString() ,sharedPref.getString("keyNotaMateriaCuatro", "0.0")!!.toString(), "Materia Cuatro"),
                arrayOf(sharedPref.getString("nombresMateriaCinco", "")!!.toString() ,sharedPref.getString("keyNotaMateriaCinco", "0.0")!!.toString(), "Materia Cinco"),
            )

            notasTest.forEach {
                nota ->
                    if(!validacionCampos(nota[0], nota[1], nota[2])){
                        errores++
                    }
            }

            if(errores==0){
                val notas: Array<Materias?> = arrayOfNulls<Materias>(5)

                var materias:Materias = Materias("", 0.0)

                for(i in 0..notasTest.size-1){
                    materias= Materias(notasTest[i][0], notasTest[i][1].toDouble())
                    notas.set(i, materias)
                    conexionBaseDeDatos.insertarNotas(materias)
                    var id:Int = conexionBaseDeDatos.getMateriasId()
                    conexionBaseDeDatos.insertarMateriasEstudiante(id, objetoEstudiante.id)
                }

                intent.putExtra("id", objetoEstudiante.id)
                startActivity(intent)

            }

        }


    }

    private fun validacionCampos(nombre: String, nota: String, seccion:String): Boolean {

        if(nombre.isEmpty() && nota.isEmpty()){
            Toast.makeText(this, "El campo nombre y nota de " + seccion + " estan vacios", Toast.LENGTH_LONG).show()
            return false
        }

        if(nombre.isEmpty()){

            Toast.makeText(this, "El campo del nombre de " + seccion + " esta vacio", Toast.LENGTH_LONG).show()
            return false

        }

        if(nota.isEmpty()){

            Toast.makeText(this, "El campo de la nota de " + seccion + " esta vacia", Toast.LENGTH_LONG).show()
            return false

        }

        else{

            return validacionRangoNotas(nota.toDouble(),seccion)
        }

    }

    private fun validacionRangoNotas(nota: Double, seccion: String): Boolean {

        if(nota >= 0.0 && nota <=  5.0){

            return true

        }else{
            Toast.makeText(this, "La nota de la seccion ${seccion} esta afuera del rango permitido", Toast.LENGTH_LONG).show()
            Toast.makeText(this, "El rango permitido es entre 0.0 y 5.0", Toast.LENGTH_LONG).show()
            return false
        }


    }


    fun modificacionCampos(elemento:String, nota:EditText, nombre: EditText, sharedPreferences: SharedPreferences) {

        when (elemento) {
            "Materia Uno" -> {
                nombre.setText(sharedPreferences.getString("nombresMateriaUno", ""))
                nota.setText(sharedPreferences.getString("keyNotaMateriaUno", ""))
            }
            "Materia Dos" -> {
                nombre.setText(sharedPreferences.getString("nombresMateriaDos", ""))
                nota.setText(sharedPreferences.getString("keyNotaMateriaDos", ""))
            }
            "Materia Tres" -> {
                nombre.setText(sharedPreferences.getString("nombresMateriaTres", ""))
                nota.setText(sharedPreferences.getString("keyNotaMateriaTres", ""))
            }
            "Materia Cuatro" -> {
                nombre.setText(sharedPreferences.getString("nombresMateriaCuatro", ""))
                nota.setText(sharedPreferences.getString("keyNotaMateriaCuatro", ""))
            }
            "Materia Cinco" -> {
                nombre.setText(sharedPreferences.getString("nombresMateriaCinco", ""))
                nota.setText(sharedPreferences.getString("keyNotaMateriaCinco", ""))
            }

        }

    }

    fun guardarEmergencia(
        elemento: String,
        nombre: EditText,
        nota: EditText,
        sharedPrefEdit: SharedPreferences.Editor,
    ){
        when(elemento){
            "Materia Uno" -> {
                sharedPrefEdit.putString("nombresMateriaUno", nombre.text.toString())
                sharedPrefEdit.putString("keyNotaMateriaUno", nota.text.toString())
                sharedPrefEdit.apply()
            }
            "Materia Dos" -> {
                sharedPrefEdit.putString("nombresMateriaDos", nombre.text.toString())
                sharedPrefEdit.putString("keyNotaMateriaDos", nota.text.toString())
                sharedPrefEdit.apply()
            }
            "Materia Tres" -> {
                sharedPrefEdit.putString("nombresMateriaTres", nombre.text.toString())
                sharedPrefEdit.putString("keyNotaMateriaTres", nota.text.toString())
                sharedPrefEdit.apply()
            }
            "Materia Cuatro" -> {
                sharedPrefEdit.putString("nombresMateriaCuatro", nombre.text.toString())
                sharedPrefEdit.putString("keyNotaMateriaCuatro", nota.text.toString())
                sharedPrefEdit.apply()
            }
            "Materia Cinco" -> {
                sharedPrefEdit.putString("nombresMateriaCinco", nombre.text.toString())
                sharedPrefEdit.putString("keyNotaMateriaCinco", nota.text.toString())
                sharedPrefEdit.apply()
            }
        }
    }


    fun modificacionHashPref(
        elemento: String,
        nombre: EditText,
        nota: EditText,
        sharedPrefEdit: SharedPreferences.Editor,
        sharedPreferences: SharedPreferences
    ){

        var viejo:String = ""


        if(sharedPreferences.getString("keyHistorialUlt","no").toString() == "no"){
            sharedPrefEdit.putString("keyHistorialUlt", elemento)
            sharedPrefEdit.apply()
        }else{
            viejo = sharedPreferences.getString("keyHistorialUlt", "").toString()
            if(!viejo.equals(elemento)){
                when(viejo){
                    "Materia Uno" -> {
                        validacionCampos(nombre.text.toString(), nota.text.toString(), viejo)
                        sharedPrefEdit.putString("nombresMateriaUno", nombre.text.toString())
                        sharedPrefEdit.putString("keyNotaMateriaUno", nota.text.toString())
                        sharedPrefEdit.putString("keyHistorialUlt", elemento)
                        sharedPrefEdit.apply()
                    }
                    "Materia Dos" -> {
                        validacionCampos(nombre.text.toString(), nota.text.toString(), viejo)
                        sharedPrefEdit.putString("nombresMateriaDos", nombre.text.toString())
                        sharedPrefEdit.putString("keyNotaMateriaDos", nota.text.toString())
                        sharedPrefEdit.putString("keyHistorialUlt", elemento)
                        sharedPrefEdit.apply()
                    }
                    "Materia Tres" -> {
                        validacionCampos(nombre.text.toString(), nota.text.toString(), viejo)
                        sharedPrefEdit.putString("nombresMateriaTres", nombre.text.toString())
                        sharedPrefEdit.putString("keyNotaMateriaTres", nota.text.toString())
                        sharedPrefEdit.putString("keyHistorialUlt", elemento)
                        sharedPrefEdit.apply()
                    }
                    "Materia Cuatro" -> {
                        validacionCampos(nombre.text.toString(), nota.text.toString(), viejo)
                        sharedPrefEdit.putString("nombresMateriaCuatro", nombre.text.toString())
                        sharedPrefEdit.putString("keyNotaMateriaCuatro", nota.text.toString())
                        sharedPrefEdit.putString("keyHistorialUlt", elemento)
                        sharedPrefEdit.apply()
                    }
                    "Materia Cinco" -> {
                        validacionCampos(nombre.text.toString(), nota.text.toString(), viejo)
                        sharedPrefEdit.putString("nombresMateriaCinco", nombre.text.toString())
                        sharedPrefEdit.putString("keyNotaMateriaCinco", nota.text.toString())
                        sharedPrefEdit.putString("keyHistorialUlt", elemento)
                        sharedPrefEdit.apply()
                    }
                }
            }
        }
    }
}