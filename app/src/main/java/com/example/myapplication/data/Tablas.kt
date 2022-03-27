package com.example.myapplication.data

class Tablas {

    abstract class Estudiantes{

        companion object{

            val NOMBRE_TABLA = "estudiantes"

            val _ID = "_id"

            val NOMBRES = "nombres"

            val DOCUMENTO = "documento"

            val DIRECCION = "direccion"

            val EDAD = "edad"

            val TELEFONO = "telefono"

            val CATEGORIA_ESTUDIANTE = "idClasificacionFk"

            var Estudiantes: MutableList<Estudiantes> = ArrayList()

        }

    }

    abstract class Materias {

        companion object {

            val NOMBRE_TABLA = "materias"

            val _ID = "id"

            val NOMBRE_MATERIA = "nombreMateria"

            val NOTA_MATERIA = "notaMateria"

            var Materias: MutableList<Materias> = ArrayList()
        }

    }

    abstract class EstudiantesMaterias{

        companion object {

            val NOMBRE_TABLA = "estudiantes_materias"

            val idEstudiante = "idEstudianteFk"

            val idMateria = "idMateriaFk"

            var estudiantesMaterias:MutableList<EstudiantesMaterias> = ArrayList()
        }

    }

    abstract class clasificacionEstudiantes{

        companion object{

            val NOMBRE_TABLA = "clasificacionEstudiantes"

            val idClasificacion = "idClasificacion"

            val nombreClasificacion = "nombreClasificacion"

            var clasificacionEstudiantes:MutableList<clasificacionEstudiantes> = ArrayList()

        }

    }

}