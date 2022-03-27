package com.example.myapplication.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.Categoria
import com.example.myapplication.Estudiante
import com.example.myapplication.Materias

class ConexionBaseDeDatos(context: Context): SQLiteOpenHelper(context, "promedios", null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE " + Tablas.Estudiantes.NOMBRE_TABLA + " (" +
                Tablas.Estudiantes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Tablas.Estudiantes.NOMBRES + " VARCHAR(50) NOT NULL," +
                Tablas.Estudiantes.DIRECCION + " VARCHAR(50) NOT NULL," +
                Tablas.Estudiantes.DOCUMENTO + " VARCHAR(10) NOT NULL," +
                Tablas.Estudiantes.EDAD + " VARCHAR(2) NOT NULL," +
                Tablas.Estudiantes.TELEFONO + " VARCHAR(10) NOT NULL," +
                Tablas.Estudiantes.CATEGORIA_ESTUDIANTE + " INT," +
                " FOREIGN KEY(" + Tablas.Estudiantes.CATEGORIA_ESTUDIANTE + ") REFERENCES "+ Tablas.clasificacionEstudiantes.NOMBRE_TABLA
                + "(" + Tablas.clasificacionEstudiantes.idClasificacion + ") "+
                ")"
        )

        db!!.execSQL("CREATE TABLE " + Tablas.Materias.NOMBRE_TABLA + " (" +
                Tablas.Materias._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Tablas.Materias.NOMBRE_MATERIA + " VARCHAR(20) NOT NULL," +
                Tablas.Materias.NOTA_MATERIA + " DOUBLE NOT NULL" +
                ") "
        )

        db!!.execSQL("CREATE TABLE " + Tablas.EstudiantesMaterias.NOMBRE_TABLA + " (" +
                Tablas.EstudiantesMaterias.idEstudiante + " INTEGER," +
                Tablas.EstudiantesMaterias.idMateria + " INTEGER, "
                +  " FOREIGN KEY(" + Tablas.EstudiantesMaterias.idEstudiante + ") REFERENCES "+ Tablas.Estudiantes.NOMBRE_TABLA
                + "(" + Tablas.Estudiantes._ID + "), "
                + " FOREIGN KEY(" + Tablas.EstudiantesMaterias.idMateria + ") REFERENCES "+ Tablas.Materias.NOMBRE_TABLA
                + "(" + Tablas.Materias._ID + ") "+
                ") "
        )

        db!!.execSQL("CREATE TABLE " + Tablas.clasificacionEstudiantes.NOMBRE_TABLA + " (" +
                Tablas.clasificacionEstudiantes.idClasificacion + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Tablas.clasificacionEstudiantes.nombreClasificacion + " VARCHAR(30) NOT NULL" +
                ") "
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + Tablas.Estudiantes.NOMBRE_TABLA)
        db!!.execSQL("DROP TABLE IF EXISTS " + Tablas.Materias.NOMBRE_TABLA)
        db!!.execSQL("DROP TABLE IF EXISTS " + Tablas.EstudiantesMaterias.NOMBRE_TABLA)
        onCreate(db)
    }

    fun insertarEstudiantes(estudiante: Estudiante){

        val values = ContentValues()
        val db = this.writableDatabase

        values.put(Tablas.Estudiantes.NOMBRES, estudiante.nombre)
        values.put(Tablas.Estudiantes.TELEFONO, estudiante.telefono)
        values.put(Tablas.Estudiantes.EDAD, estudiante.edad)
        values.put(Tablas.Estudiantes.DOCUMENTO, estudiante.documento)
        values.put(Tablas.Estudiantes.DIRECCION, estudiante.direccion)

        db.insert(Tablas.Estudiantes.NOMBRE_TABLA, null, values)
        db.close()
    }

    fun actualizarRegistroEstudiante(estudiante: Estudiante, categoria:Categoria){

        val values = ContentValues()

        val db = this.writableDatabase

        values.put(Tablas.Estudiantes.CATEGORIA_ESTUDIANTE, categoria.id)

        db.update(Tablas.Estudiantes.NOMBRE_TABLA, values, "_id=" + estudiante.id, null)

        db.close()
    }

    fun buscarCategoriasNombre(nombreCategoria: String): Categoria {

        val bd = this.writableDatabase

        var categoria: Categoria? = null

        val consulta = bd.rawQuery("SELECT * FROM " + Tablas.clasificacionEstudiantes.NOMBRE_TABLA + " WHERE nombreClasificacion='" + nombreCategoria + "'",  null)

        if(consulta.moveToFirst()){

            categoria = Categoria(consulta.getInt(0), consulta.getString(1))

        }

        return categoria!!

    }


    fun getEstudianteCategoriaEstudiante(categoria: Categoria):Int{

        val bd = this.writableDatabase

        var conteo: Int = 0

        val consulta = bd.rawQuery("SELECT count(*) FROM " + Tablas.Estudiantes.NOMBRE_TABLA + " WHERE idClasificacionFk=" + categoria.id,  null)

        if(consulta.moveToFirst()){

            conteo = consulta.getInt(0)

        }

        return conteo
    }


    fun insertarCategorias(){

        val values = ContentValues()

        val db = this.writableDatabase

        values.put(Tablas.clasificacionEstudiantes.nombreClasificacion, "perdio")

        db.insert(Tablas.clasificacionEstudiantes.NOMBRE_TABLA, null, values)

        values.clear()

        values.put(Tablas.clasificacionEstudiantes.nombreClasificacion, "gano")

        db.insert(Tablas.clasificacionEstudiantes.NOMBRE_TABLA, null, values)

        values.clear()

        values.put(Tablas.clasificacionEstudiantes.nombreClasificacion, "recuperacion")

        db.insert(Tablas.clasificacionEstudiantes.NOMBRE_TABLA, null, values)

        db.close()

    }

    fun tablaClasificacionConteo(): Int{

        val db = this.writableDatabase

        var conteo: Int? = null

        val consulta = db.rawQuery("SELECT count(*) FROM " + Tablas.clasificacionEstudiantes.NOMBRE_TABLA, null)

        if(consulta.moveToFirst()){
            conteo = consulta.getInt(0)

        }
        db.close()

        return  conteo!!
    }

    fun tablaEstudiantesConteo(): Int{

        val db = this.writableDatabase

        var conteo: Int? = null

        val consulta = db.rawQuery("SELECT count(*) FROM " + Tablas.Estudiantes.NOMBRE_TABLA, null)

        if(consulta.moveToFirst()){
            conteo = consulta.getInt(0)

        }
        db.close()

        return  conteo!!

    }


    fun insertarNotas(materias:Materias){

        val values = ContentValues()
        val db = this.writableDatabase

        values.put(Tablas.Materias.NOMBRE_MATERIA, materias.nombreMateria)
        values.put(Tablas.Materias.NOTA_MATERIA, materias.notaMateria)

        db.insert(Tablas.Materias.NOMBRE_TABLA, null, values)
        db.close()

    }

    fun insertarMateriasEstudiante(idMateria:Int, idEstudiante: Int){

        val values = ContentValues()
        val db = this.writableDatabase

        values.put(Tablas.EstudiantesMaterias.idEstudiante, idEstudiante)
        values.put(Tablas.EstudiantesMaterias.idMateria, idMateria)

        db.insert(Tablas.EstudiantesMaterias.NOMBRE_TABLA, null, values)

        db.close()

    }

    fun getEstudiantes(): Estudiante {

        val bd = this.writableDatabase

        var estudiante: Estudiante? = null

        val consulta = bd.rawQuery("SELECT * FROM " + Tablas.Estudiantes.NOMBRE_TABLA,  null)

        if(consulta.moveToLast()){

            estudiante = Estudiante(consulta.getInt(0), consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4), consulta.getString(5))

        }

        bd.close()

        return estudiante!!

    }

    fun getMateriasId(): Int{

        val bd = this.writableDatabase

        var materias: Int? = null

        val consulta = bd.rawQuery("SELECT id FROM " + Tablas.Materias.NOMBRE_TABLA, null)

        if(consulta.moveToLast()){

            materias = consulta.getInt(0)

        }

        bd.close()

        return materias!!
    }

    fun getEstudiantesId(idEstudiante: Int):Estudiante{

        val bd = this.writableDatabase

        var estudiante:Estudiante? = null

        val consulta = bd.rawQuery("SELECT _id, nombres, documento, direccion, edad, telefono FROM " + Tablas.Estudiantes.NOMBRE_TABLA + " WHERE _id=" + idEstudiante, null)

        if(consulta.moveToFirst()){

            estudiante = Estudiante(consulta.getInt(0), consulta.getString(1), consulta.getString(2), consulta.getString(3), consulta.getString(4), consulta.getString(5))

        }

        bd.close()

        return  estudiante!!

    }

    fun getEstudiantesMaterias(idEstudiante:Int):ArrayList<Materias>{

        val bd = this.writableDatabase

        var materiasLista = ArrayList<Materias>()


        //select m.nombreMateria, m.notaMateria from estudiantes e inner join estudiantes_materias
        // em on e._id = em.idEstudianteFk inner join materias m on em.idMateriaFk = m.id where e._id=1;
        val consulta = bd.rawQuery(
            "SELECT m." + Tablas.Materias.NOMBRE_MATERIA + ", m." + Tablas.Materias.NOTA_MATERIA + " FROM " + Tablas.Estudiantes.NOMBRE_TABLA + " e JOIN " + Tablas.EstudiantesMaterias.NOMBRE_TABLA +
                    " em ON e._id=em.idEstudianteFk JOIN " + Tablas.Materias.NOMBRE_TABLA + " m ON m.id=em.idMateriaFk WHERE e._id=" + idEstudiante, null)

        if(consulta.moveToFirst()){
            do{
                var materias: Materias = Materias(consulta.getString(0), consulta.getDouble(1))

                materiasLista.add(materias)
            }while (consulta.moveToNext())
        }

        bd.close()

        return materiasLista

    }
}