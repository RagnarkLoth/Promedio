package com.example.myapplication

import android.util.Log
import android.widget.Button
import android.widget.EditText
import java.io.Serializable

class Estudiante(id:Int ,nombre:String, documento: String, direccion:String, edad:String, telefono:String) :
    Serializable {

    var id:Int = id
        get() = field
        set(value){
            field=value
        }

     var nombre:String = nombre
        get() = field
        set(value) {
            field = value
        }

    var documento:String = documento
        get() = field
        set(value) {
            field = value
        }

    var direccion:String = direccion
        get() = field
        set(value) {
            field = value
        }

    var edad:String = edad
        get() = field
        set(value) {
            field = value
        }

    var telefono:String = telefono
        get() = field
        set(value) {
            field = value
        }


    fun verificarInformacionEstudiante(campo:EditText):Boolean{
        return campo.text.toString().isEmpty()
    }



}