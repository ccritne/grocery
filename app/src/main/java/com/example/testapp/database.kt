package com.example.testapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class Food(
    var id: Int,
    var name: String,
    var grams: Int,
    var price: Float,
    var date: String,
    var momentSelector: Int,
    var idParent: Int
)

class DbHelper(context: Context?) :
    SQLiteOpenHelper(context, "menu", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {

        val q = """CREATE TABLE IF NOT EXISTS menu  (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT
                                                   NOT NULL,
                        name           TEXT (15)   NOT NULL,
                        grams          INTEGER (4) NOT NULL,
                        momentSelector INTEGER (2) NOT NULL,
                        date           TEXT (10)   NOT NULL,
                        price          REAL (4, 2) DEFAULT (-1.0),
                        idParent       INTEGER     DEFAULT ( -1)
                    );
            """
        db.execSQL(q)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}

class DbManager
    (ctx: Context?) {
    private val dbHelper = DbHelper(ctx)
    fun insertFood(
        name: String,
        grams: Int,
        momentSelector: Int,
        date: String,
        price: Float = -1.0f,
        idParent: Int = -1
        ) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues()

        cv.put("name", name)
        cv.put("grams", grams)
        cv.put("momentSelector", momentSelector)
        cv.put("date", date)
        if (price != -1.0f)
            cv.put("price", price)
        if (idParent != -1)
            cv.put("idParent", idParent)

        try {
            val cursor = db.insertOrThrow("menu", null, cv)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("INSERT", "Couldn't insert data : ${e.message.toString()}")
        }
    }

    fun drop(){
        val db = dbHelper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS menu")
    }

    fun createIfNotExists(){
        try {
            val db = dbHelper.readableDatabase

            val query = """CREATE TABLE IF NOT EXISTS menu  (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT
                                                   NOT NULL,
                        name           TEXT (15)   NOT NULL,
                        grams          INTEGER (4) NOT NULL,
                        momentSelector INTEGER (2) NOT NULL,
                        date           TEXT (10)   NOT NULL,
                        price          REAL (4, 2) DEFAULT (-1.0),
                        idParent       INTEGER     DEFAULT ( -1)
                    );"""

            db.execSQL(query)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
        }
    }

    fun selectFromDay(
        date: String,
        onDataTaken: (ArrayList<Food>) -> Unit) {

        var cursor: Cursor? = null

        try {
            val db = dbHelper.readableDatabase

            val query = "SELECT * FROM menu WHERE date='$date';"

            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("LOAD", "Couldn't load data : ${e.message.toString()}")
        }

        var id: Int
        var name: String
        var grams: Int
        var momentSelector: Int
        var price: Float
        var idParent: Int

        val listFood : ArrayList<Food> = ArrayList<Food>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                grams = cursor.getInt(cursor.getColumnIndexOrThrow("grams"))
                momentSelector = cursor.getInt(cursor.getColumnIndexOrThrow("momentSelector"))
                price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"))
                idParent = cursor.getInt(cursor.getColumnIndexOrThrow("idParent"))

                val food = Food(
                    id = id,
                    name = name,
                    grams = grams,
                    momentSelector = momentSelector,
                    price = price,
                    date = date,
                    idParent = idParent
                )

                listFood.add(food)

            } while (cursor.moveToNext())
            cursor.close()
        }

        onDataTaken(listFood)
    }

    fun deleteFood(id: Int){

        try {
            val db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM menu WHERE id=$id")
        }
        catch (e: SQLiteException){
            e.printStackTrace()
            Log.e("DELETE", "Couldn't delete data: ${e.message.toString()}")
        }

    }
}