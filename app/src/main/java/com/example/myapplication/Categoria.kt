package com.example.myapplication

class Categoria(id:Int, nombreCategoria: String) {

    var id:Int = id
        get() = field
        set(value){
            field=value
        }

    var nombreCategoria:String = nombreCategoria
        get() = field
        set(value) {
            field = value
        }
}