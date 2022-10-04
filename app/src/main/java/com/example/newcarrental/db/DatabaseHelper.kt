package com.example.newcarrental.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.sql.SQLException

const val TAG = "DatabaseHelper"

class DatabaseHelper internal constructor(private val myContext: Context) :
    SQLiteOpenHelper(myContext, DB_NAME, null, SCHEMA) {
    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
    fun createDB() {
        var myInput: InputStream? = null
        var myOutput: OutputStream? = null
        try {
            val file = File(DB_PATH)
            if (!file.exists()) {
                myInput = myContext.assets.open(DB_NAME)
                val outFileName = DB_PATH
                myOutput = FileOutputStream(outFileName)
                val buffer = ByteArray(1024)
                var length: Int
                while (myInput.read(buffer).also { length = it } > 0) {
                    myOutput.write(buffer, 0, length)
                }
                myOutput.flush()
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.message!!)
        } finally {
            try {
                myOutput?.close()
                myInput?.close()
            } catch (ex: java.lang.Exception) {
                Log.d(TAG, ex.message!!)
            }
        }
    }

    @Throws(SQLException::class)
    fun open(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE)
    }

    companion object {
        private lateinit var DB_PATH: String
        private const val DB_NAME = "car_rental3.db"
        private const val SCHEMA = 1
        const val TABLE_C = "cars"
        const val TABLE_U = "users"
        const val TABLE_O = "orders"
        const val TABLE_F = "favorites"

        //table cars
        const val COLUMN_ID_C = "_id"
        const val COLUMN_I_C = "image"
        const val COLUMN_N_C = "name"
        const val COLUMN_P_C = "price"
        const val COLUMN_HP_C = "horse_power"
        const val COLUMN_AC_C = "acceleration"
        const val COLUMN_Y_C = "year"

        //table favorites
        const val COLUMN_ID_F = "_id"
        const val COLUMN_C_ID_F = "car_id"
        const val COLUMN_U_ID_F = "user_id"
    }

    init {
        DB_PATH = myContext.filesDir.path + DB_NAME
    }
}