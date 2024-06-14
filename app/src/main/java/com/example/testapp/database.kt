package com.example.testapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.testapp.body.fromGoogleToApp

data class Food(
    var id: Int = 0,
    var name: String = "",
    var grams: Int = 0,
    var date: String = "",
    var momentSelector: Int = 0,
    var eaten : Boolean = false,
    var checked: Boolean = false,
    var price: Float = -1.0f,
    var idParent: Int = -1
)

data class ShoppingCartItem(
    var id: Int = 0,
    var name: String = "",
    var total: Int = 0,
    var checkedTotal: Int = 0,
)



class DbHelper(context: Context?) :
    SQLiteOpenHelper(context, "menu", null, 1) {

    fun createIfNotExists(db: SQLiteDatabase){
        try {

            val query = """CREATE TABLE IF NOT EXISTS menu  (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT
                                                   NOT NULL,
                        name           TEXT (15)   NOT NULL,
                        grams          INTEGER (4) NOT NULL,
                        momentSelector INTEGER (2) NOT NULL,
                        date           TEXT (10)   NOT NULL,
                        eaten          INTEGER     NOT NULL DEFAULT(0),
                        checked        INTEGER     NOT NULL DEFAULT(0),
                        checkedTotal        INTEGER     NOT NULL DEFAULT(0),
                        price          REAL (4, 2) DEFAULT (-1.0),
                        idParent       INTEGER     DEFAULT ( -1)
                    );"""

            db.execSQL(query)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        createIfNotExists(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}

class DbManager
    (ctx: Context?) {
    private val dbHelper = DbHelper(ctx)

    fun fillDataWeek(){
        fromGoogleToApp(this)
    }
    fun recreateDb(){
        drop()
        val db = dbHelper.writableDatabase
        dbHelper.createIfNotExists(db)
    }


    fun insertFood(
        name: String,
        grams: Int,
        momentSelector: Int,
        date: String,
        price: Float = -1.0f,
        idParent: Int = -1
        ) {


        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()

            cv.put("name", name)
            cv.put("grams", grams)
            cv.put("momentSelector", momentSelector)
            cv.put("date", date)
            cv.put("checked", false)
            cv.put("checkedTotal", 0)
            cv.put("eaten", false)
            if (price != -1.0f)
                cv.put("price", price)
            if (idParent != -1)
                cv.put("idParent", idParent)


            db.insertOrThrow("menu", null, cv)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("INSERT", "Couldn't insert data : ${e.message.toString()}")
        }
    }

    fun drop(){
        val db = dbHelper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS menu")
    }

    fun updateCart(name: String, startDate: String, endDate: String, newChecked: Boolean, newValue: Int){
        println("$name $startDate $endDate $newChecked $newValue")

        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put("checked", newChecked)
            cv.put("checkedTotal", newValue)

            db.update("menu", cv, "name=? and date >= ? and date <= ? ", arrayOf(name, startDate, endDate))
        }catch (e: SQLiteException){
            e.printStackTrace()
            Log.i("UPDATE", "Couldn't update: ${e.message.toString()}")
        }
    }

    fun updateItem(id: Int, newGrams: Int){
        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put("grams", newGrams)

            db.update("menu", cv, "id = ?", arrayOf(id.toString()))
        }catch (e: SQLiteException){
            e.printStackTrace()
            Log.i("UPDATE", "Couldn't update: ${e.message.toString()}")
        }
    }

    fun updateEaten(id: Int, newState: Boolean){
        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put("eaten", newState)

            db.update("menu", cv, "id = ?", arrayOf(id.toString()))
        }catch (e: SQLiteException){
            e.printStackTrace()
            Log.i("UPDATE", "Couldn't update: ${e.message.toString()}")
        }
    }

    fun selectBoughtAggregate(
        startDate: String,
        endDate: String,
        onDataTaken: (List<ShoppingCartItem>) -> Unit
    ){
        var cursor : Cursor? = null

        try {
            val db = dbHelper.readableDatabase
            val query = """
                SELECT 
                id,
                name,
                checkedTotal
                from menu 
                where date>="$startDate" and date <="$endDate" and checkedTotal > 0 group by name
            """.trimIndent()
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("LOAD", "Couldn't load data : ${e.message.toString()}")
        }

        var food: ShoppingCartItem

        val listFood : MutableList<ShoppingCartItem> = mutableListOf()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                food = ShoppingCartItem()
                food.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                food.name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                food.checkedTotal = cursor.getInt(cursor.getColumnIndexOrThrow("checkedTotal"))

                listFood.add(food)

            } while (cursor.moveToNext())
            cursor.close()
        }

        onDataTaken(listFood)
    }

    fun selectUniqueAggregate(
        startDate: String,
        endDate: String,
        onDataTaken: (List<ShoppingCartItem>) -> Unit
        )  {

        var cursor : Cursor? = null


        try {
            val db = dbHelper.readableDatabase
            val query = """
                SELECT 
                id,
                name, 
                CASE 
                    WHEN grams = 0 THEN count(name) 
                    ELSE sum(grams) 
                END as total,
                checkedTotal
                from menu 
                where date>="$startDate" and date <="$endDate" group by name
            """.trimIndent()
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("LOAD", "Couldn't load data : ${e.message.toString()}")
        }

        var food: ShoppingCartItem

        val listFood : MutableList<ShoppingCartItem> = mutableListOf()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                food = ShoppingCartItem()
                food.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                food.name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                food.total = cursor.getInt(cursor.getColumnIndexOrThrow("total"))
                food.checkedTotal = cursor.getInt(cursor.getColumnIndexOrThrow("checkedTotal"))

                listFood.add(food)

            } while (cursor.moveToNext())
            cursor.close()
        }

        onDataTaken(listFood)

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

