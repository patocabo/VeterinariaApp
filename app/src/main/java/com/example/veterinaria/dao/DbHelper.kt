package com.example.veterinaria.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.ArrayMap
import android.util.Log
import com.example.veterinaria.model.Diagnostico
import com.example.veterinaria.model.Turno
import com.example.veterinaria.model.Veterinario
import java.lang.Exception

class DbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "veterinaria.db"
        private val DATABASE_VERSION = 1

        val T_TABLE_NAME = "turnos"
        val T_COLUMN_ID = "id"
        val T_COLUMN_TIPO = "tipo"
        val T_COLUMN_NOMBRE = "nombre"
        val T_COLUMN_RAZA = "raza"
        val T_COLUMN_EDAD = "edad"
        val T_COLUMN_RAZON = "razon"
        val T_COLUMN_DIA_TURNO = "dia"
        val T_COLUMN_VET_ID = "veterinario_id"

        val V_TABLE_NAME = "veterinarios"
        val V_COLUMN_ID = "id"
        val V_COLUMN_NOMBRE = "nombre"

        val D_TABLE_NAME = "diagnosticos"
        val D_COLUMN_ID = "id"
        val D_COLUMN_TURNO_ID = "turno_id"
        val D_COLUMN_CAUSAS = "causas"
        val D_COLUMN_MEDICAMENTOS = "medicamentos"


    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createVetTable =
            ("CREATE TABLE " + V_TABLE_NAME + " ( " + V_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    V_COLUMN_NOMBRE + " TEXT ) ")


        val createTurnosTable =
            ("CREATE TABLE " + T_TABLE_NAME + " ( " + T_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    T_COLUMN_VET_ID + " INTEGER, " +
                    T_COLUMN_NOMBRE + " TEXT, " +
                    T_COLUMN_TIPO + " TEXT, " +
                    T_COLUMN_RAZA + " TEXT, " +
                    T_COLUMN_EDAD + " TEXT, " +
                    T_COLUMN_RAZON + " TEXT, " +
                    T_COLUMN_DIA_TURNO + " TEXT, " +
                    "FOREIGN KEY (" + T_COLUMN_VET_ID + ") REFERENCES " + V_TABLE_NAME + "(" + V_COLUMN_ID + ")"
                    + " ) ")
        val createDiagnosticsTable =
            ("CREATE TABLE " + D_TABLE_NAME + " ( " + D_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    D_COLUMN_CAUSAS + " TEXT, " +
                    D_COLUMN_MEDICAMENTOS + " TEXT, " +
                    D_COLUMN_TURNO_ID + " INTEGER, " +
                    "FOREIGN KEY (" + D_COLUMN_TURNO_ID + ") REFERENCES " + T_TABLE_NAME + "(" + T_COLUMN_ID + ")"
                    + " ) ")




        db?.execSQL(createVetTable)
        seedVetTable(db)
        db?.execSQL(createTurnosTable)
        db?.execSQL(createDiagnosticsTable)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + D_TABLE_NAME)
        db?.execSQL("DROP TABLE IF EXISTS " + T_TABLE_NAME)
        db?.execSQL("DROP TABLE IF EXISTS " + V_TABLE_NAME)
        onCreate(db)
    }

    fun saveTurno(turno: Turno, vet: Veterinario): Boolean {
        try {

            val db = this.writableDatabase
            val values = ContentValues()

            values.put(T_COLUMN_NOMBRE, turno.nombre)
            values.put(T_COLUMN_DIA_TURNO, turno.turno)
            values.put(T_COLUMN_TIPO, turno.tipo)
            values.put(T_COLUMN_EDAD, turno.edad)
            values.put(T_COLUMN_RAZA, turno.raza)
            values.put(T_COLUMN_RAZON, turno.razon)
            values.put(T_COLUMN_VET_ID, vet.id)

            db.insert(T_TABLE_NAME, null, values)
            return true

        } catch (e: Exception) {
            Log.e("SaveMascota: ", e.message.toString())
        }
        return false
    }

    private fun seedVetTable(db: SQLiteDatabase?): Boolean {
        try {
            for (item in arrayListOf<String>("Pato", "Martin", "Rodrigo", "Julian", "Juan")) {
                val values = ContentValues()
                values.put(V_COLUMN_NOMBRE, item)
                db?.insert(V_TABLE_NAME, null, values)

            }
            return true

        } catch (e: Exception) {
            Log.e("SaveMascota: ", e.message.toString())
        }
        return false
    }

    fun getVetsForDate(date: String, context: Context): ArrayList<Veterinario> {
        val db = this.readableDatabase
        val listaVeterinarios = ArrayList<Veterinario>()
        val query =
            "SELECT $V_TABLE_NAME.$V_COLUMN_NOMBRE, $V_TABLE_NAME.$V_COLUMN_ID, COUNT($T_TABLE_NAME.$T_COLUMN_ID) FROM $V_TABLE_NAME LEFT JOIN " +
                    "$T_TABLE_NAME ON $V_TABLE_NAME.$V_COLUMN_ID = $T_TABLE_NAME.$T_COLUMN_VET_ID AND " +
                    "$T_TABLE_NAME.$T_COLUMN_DIA_TURNO = '$date' GROUP BY $V_TABLE_NAME.$V_COLUMN_ID" +
                    " HAVING COUNT(*)<5"
        Log.e("LISTADO", query)

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndex(V_COLUMN_NOMBRE))
                val id = cursor.getString(cursor.getColumnIndex(V_COLUMN_ID)).toString().toInt()
                listaVeterinarios.add(Veterinario(nombre, id))
            } while (cursor.moveToNext())
        }

        return listaVeterinarios

    }

    fun getAllVet(): ArrayList<Veterinario> {
        val db = this.readableDatabase
        val listaVeterinarios = ArrayList<Veterinario>()
        val query =
            "SELECT * FROM $V_TABLE_NAME"

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndex(V_COLUMN_NOMBRE))
                val id = cursor.getString(cursor.getColumnIndex(V_COLUMN_ID)).toString().toInt()
                listaVeterinarios.add(Veterinario(nombre, id))
            } while (cursor.moveToNext())
        }

        return listaVeterinarios
    }

    fun getTurnosByDateAndVetId(date: String, id: Int): ArrayMap<Turno, IntArray> {
        val db = this.readableDatabase
        var arrayMap: ArrayMap<Turno, IntArray> = ArrayMap<Turno, IntArray>()
        val query =
            "SELECT * , CASE WHEN EXISTS (SELECT * FROM $D_TABLE_NAME WHERE $D_TABLE_NAME.$D_COLUMN_TURNO_ID = $T_TABLE_NAME.$T_COLUMN_ID)" +
                    " THEN 1 ELSE 0 END AS DIAGNOSTICADO FROM $T_TABLE_NAME" +
                    " WHERE $T_TABLE_NAME.$T_COLUMN_VET_ID = $id AND $T_TABLE_NAME.$T_COLUMN_DIA_TURNO = '$date'"

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val tipo = cursor.getString(cursor.getColumnIndex(T_COLUMN_TIPO))
                val nombre = cursor.getString(cursor.getColumnIndex(T_COLUMN_NOMBRE))
                val raza = cursor.getString(cursor.getColumnIndex(T_COLUMN_RAZA))
                val edad = cursor.getString(cursor.getColumnIndex(T_COLUMN_EDAD)).toString().toInt()
                val razon = cursor.getString(cursor.getColumnIndex(T_COLUMN_RAZON))
                val turno = cursor.getString(cursor.getColumnIndex(T_COLUMN_DIA_TURNO))
                val diagnosticado =
                    cursor.getString(cursor.getColumnIndex("DIAGNOSTICADO")).toString().toInt()
                val turno_id =
                    cursor.getString(cursor.getColumnIndex(T_COLUMN_ID)).toString().toInt()
                intArrayOf(diagnosticado, turno_id)
                arrayMap[Turno(tipo, nombre, raza, edad, razon, turno)] =
                    intArrayOf(diagnosticado, turno_id)
            } while (cursor.moveToNext())
        }

        return arrayMap
    }

    fun countTurnoByDate(date: String): Int {
        val db = this.readableDatabase
        val listaTurnos = ArrayList<Turno>()
        val query =
            "SELECT COUNT(*) AS CANTIDAD FROM $T_TABLE_NAME WHERE $T_TABLE_NAME.$T_COLUMN_DIA_TURNO = '$date'"
        val cursor = db.rawQuery(query, null)
        var cantidad = 0
        if (cursor.moveToFirst()) {
            do {
                cantidad = cursor.getString(cursor.getColumnIndex("CANTIDAD")).toString().toInt()
            } while (cursor.moveToNext())

        }
        Log.e("CANTIDAD", cantidad.toString())
        return cantidad
    }

    fun insertDiagnostic(diagnostico: Diagnostico, turno_id: Int): Boolean {
        try {

            val db = this.writableDatabase
            val values = ContentValues()

            values.put(D_COLUMN_CAUSAS, diagnostico.diagnostico)
            values.put(D_COLUMN_MEDICAMENTOS, diagnostico.medicacion)
            values.put(D_COLUMN_TURNO_ID, turno_id)

            db.insert(D_TABLE_NAME, null, values)
            return true

        } catch (e: Exception) {
            Log.e("SaveDiagnostico: ", e.message.toString())
        }
        return false
    }

    fun getDiagnosticoById(id: Int): Diagnostico {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM $D_TABLE_NAME WHERE $D_TABLE_NAME.$D_COLUMN_TURNO_ID = $id"

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val medicacion = cursor.getString(cursor.getColumnIndex(D_COLUMN_MEDICAMENTOS))
            val diagnostico = cursor.getString(cursor.getColumnIndex(D_COLUMN_CAUSAS))
            return Diagnostico(diagnostico, medicacion)
        }
        return Diagnostico("", "")
    }

    fun getRegistersByDate(date: String): ArrayMap<Veterinario, IntArray> {
        val db = this.readableDatabase
        var arrayMap: ArrayMap<Veterinario, IntArray> = ArrayMap<Veterinario, IntArray>()
        val query =
            "SELECT $V_TABLE_NAME.$V_COLUMN_NOMBRE, $V_TABLE_NAME.$V_COLUMN_ID, COUNT($T_TABLE_NAME.$T_COLUMN_ID) AS TOTAL FROM $V_TABLE_NAME LEFT JOIN " +
                    "$T_TABLE_NAME ON $V_TABLE_NAME.$V_COLUMN_ID = $T_TABLE_NAME.$T_COLUMN_VET_ID AND " +
                    "$T_TABLE_NAME.$T_COLUMN_DIA_TURNO = '$date' GROUP BY $V_TABLE_NAME.$V_COLUMN_ID"


        Log.e("LISTADOO", query)
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {

                val nombre = cursor.getString(cursor.getColumnIndex(V_COLUMN_NOMBRE))
                val total = cursor.getString(cursor.getColumnIndex("TOTAL")).toString().toInt()
                val id = cursor.getString(cursor.getColumnIndex(V_COLUMN_ID)).toString().toInt()

                arrayMap[Veterinario(nombre, 1)] = intArrayOf(total, id)
            } while (cursor.moveToNext())
        }

        return arrayMap
    }
}

