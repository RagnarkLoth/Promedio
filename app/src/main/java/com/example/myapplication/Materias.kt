package com.example.myapplication

import android.util.Log


class Materias(nombreMateria: String, notaMateria: Double) {

    var nombreMateria:String = nombreMateria
        get() = field
        set(value) {
            field = value
        }

    var notaMateria:Double = notaMateria
        get() = field
        set(value) {
            field = value
        }

    fun sacarPromedioMaterias(notas:Array<Materias?>):Double{
        var promedio:Double = 0.0

        notas.forEach {
            nota ->
                promedio += nota!!.notaMateria
        }

        promedio/=5

        return promedio
    }

}