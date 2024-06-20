package com.example.grocery.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.grocery.utilities.Item
import com.example.grocery.utilities.Screen
import com.example.grocery.utilities.Units
import com.example.grocery.utilities.fromGoogleToApp


class DbHelper(context: Context?) :
    SQLiteOpenHelper(context, "menu", null, 1) {

    fun createPlanIfNotExists(db: SQLiteDatabase){
        try {

            val query = """CREATE TABLE IF NOT EXISTS menu  (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT
                                                   NOT NULL,
                        idParent       INTEGER     DEFAULT ( -1),
                        idInventory    INTEGER     NOT NULL,
                        amount         INTEGER     NOT NULL,
                        momentSelector INTEGER (2) NOT NULL,
                        date           TEXT (10)   NOT NULL,
                        eaten          INTEGER     NOT NULL DEFAULT(0)
                    );"""

            db.execSQL(query)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
        }
    }

    fun createInventoryIfNotExists(db: SQLiteDatabase){
        try {

            val query = """CREATE TABLE IF NOT EXISTS inventory  (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT
                                                   NOT NULL,
                        name           TEXT        UNIQUE NOT NULL,
                        amount         INTEGER     NOT NULL,
                        unit           INTEGER     NOT NULL,
                        place          TEXT        NOT NULL
                    );"""

            db.execSQL(query)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        createPlanIfNotExists(db)
        createInventoryIfNotExists(db)
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
        dbHelper.createInventoryIfNotExists(db)
        dbHelper.createPlanIfNotExists(db)
    }

    fun insertItemInventory(item: Item) : Long{

        val db = dbHelper.writableDatabase
        val cv = ContentValues()

        cv.put("name", item.name)
        cv.put("amount", 0)
        cv.put("unit", item.unit.name)
        cv.put("place", item.place.name)

        return db.insert("inventory", null, cv)

    }

    fun getUnitOf(name: String) : Units?{
        var toRet: Units? = null
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT unit FROM inventory WHERE name=?", arrayOf(name))
            if (cursor?.moveToFirst() == true)
                toRet = Units.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("unit")))
            cursor.close()
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("GET", "Couldn't get data : ${e.message.toString()}")
        }
        return toRet
    }


    fun insertItem(item: Item) {


        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()

            cv.put("idInventory", item.idInventory)
            cv.put("amount", item.amount)
            cv.put("momentSelector", item.momentSelector)
            cv.put("date", item.date)
            cv.put("checked", false)
            cv.put("place", item.place.name)
            if (item.price != -1.0f)
                cv.put("price", item.price)
            if (item.idParent != -1)
                cv.put("idParent", item.idParent)


            db.insertOrThrow("menu", null, cv)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("INSERT", "Couldn't insert data : ${e.message.toString()}")
        }
    }

    private fun drop() {
        val db = dbHelper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS menu")
        db.execSQL("DROP TABLE IF EXISTS inventory")
    }

    fun itemExists(name: String) : Pair<Int, Boolean> {


        try {
            val db = dbHelper.readableDatabase

            val cursor = db.rawQuery("""SELECT id, COUNT(*) as exist FROM inventory WHERE name=? """, arrayOf(name))

            if (cursor != null && cursor.moveToFirst() && cursor.getInt(cursor.getColumnIndexOrThrow("exist")) == 1 ) {
                val toReturn = Pair(cursor.getInt(cursor.getColumnIndexOrThrow("id")), true)
                cursor.close()
                return toReturn
            }

            return Pair(-1, false)

        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("CHECK EXISTENCE", "Couldn't check existence : ${e.message.toString()}")

        }

        return Pair(-1, false)
    }


    fun updateInventoryItem(item: Item){
        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()

            cv.put("name", item.name)
            cv.put("amount", item.amount)
            cv.put("unit", item.unit.name)

            db.update("inventory", cv, "id=?", arrayOf(item.id.toString()))
        }catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("UPDATE", "Couldn't update data : ${e.message.toString()}")
        }
    }

    fun deleteInventoryItem(id: Int){
        try {
            val db = dbHelper.writableDatabase

            db.delete("inventory", "id=?", arrayOf(id.toString()))
        }catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("UPDATE", "Couldn't update data : ${e.message.toString()}")
        }
    }

    fun updateCart(idInventory: Int,  newValue: Int){

        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put("amount", newValue)

            db.update("inventory", cv, "id=?", arrayOf(idInventory.toString()))
        }catch (e: SQLiteException){
            e.printStackTrace()
            Log.i("UPDATE", "Couldn't update: ${e.message.toString()}")
        }
    }

    fun getAllItems() : List<String> {

        var cursor: Cursor? = null

        try {
            val db = dbHelper.readableDatabase
            cursor = db.rawQuery("SELECT name FROM inventory", null)
        }catch (e: SQLiteException){
            e.printStackTrace()
            Log.i("SELECTION", "Couldn't select: ${e.message.toString()}")
        }

        if(cursor != null && cursor.moveToFirst()){
            val list: MutableList<String> = mutableListOf()
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                if (name.isNotEmpty())
                    list.add(name)
            }while (cursor.moveToNext())
            cursor.close()
            return list
        }

        return emptyList<String>()
    }

    fun updatePlanItem(item: Item) : Int{

        val db = dbHelper.writableDatabase
        val cv = ContentValues()

        cv.put("amount", item.amount)
        cv.put("idInventory", item.idInventory)
        cv.put("momentSelector", item.momentSelector)
        cv.put("date", item.date)

        return db.update("menu", cv, "id = ?", arrayOf(item.id.toString()))
    }

    fun updatePlanChecked(id: Int, newState: Boolean){
        try {
            val db = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put("checked", newState)

            db.update("menu", cv, "id = ?", arrayOf(id.toString()))
        }catch (e: SQLiteException){
            e.printStackTrace()
            Log.i("UPDATE", "Couldn't update: ${e.message.toString()}")
        }
    }

    fun selectInventoryItems(
    ) : List<Item>{
        var cursor : Cursor? = null

        try {
            val db = dbHelper.readableDatabase
            val query = """
                SELECT 
                    id,
                    name,
                    amount,
                    unit
                from inventory
            """.trimIndent()
            cursor = db.rawQuery(query, null)

            var item: Item

            val listItem : MutableList<Item> = mutableListOf()

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    item = Item(cursor, Screen.Inventory)

                    listItem.add(item)

                } while (cursor.moveToNext())
                cursor.close()
            }

            return listItem

        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("LOAD", "Couldn't load data : ${e.message.toString()}")
        }



        return emptyList()
    }

    fun selectShoppingCartInRange(
            startDate: String,
            endDate: String,
            onDataTaken: (MutableList<Item>) -> Unit
    )  {

    var cursor : Cursor? = null


    try {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT 
            menu.id,
            inventory.id as idInventory, 
            inventory.name, 
            inventory.amount as amountInventory,
            CASE 
                WHEN menu.amount = 0 THEN count(menu.idInventory) 
                ELSE sum(menu.amount) 
            END as amount,
            inventory.unit
            from menu 
                inner join inventory ON menu.idInventory = inventory.id
            where inventory.id = menu.idInventory and date>="$startDate" and date <="$endDate" group by name
        """.trimIndent()
        cursor = db.rawQuery(query, null)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("LOAD", "Couldn't load data : ${e.message.toString()}")
    }

    var item: Item

    val listItem : MutableList<Item> = mutableListOf()

    if (cursor != null && cursor.moveToFirst()) {
        do {
            item = Item(cursor, Screen.ShoppingCart)

            listItem.add(item)

        } while (cursor.moveToNext())
        cursor.close()
    }
    onDataTaken(listItem)

}



    fun selectPlanForDay(
        date: String,
        onDataTaken: (ArrayList<Item>) -> Unit) {

        var cursor: Cursor? = null

        try {
            val db = dbHelper.readableDatabase

            val query = """
                SELECT 
                    menu.id,
                    menu.idInventory,
                    inventory.name,
                    menu.amount,
                    menu.date,
                    inventory.unit,
                    menu.momentSelector,
                    menu.eaten
                FROM menu
                    INNER JOIN inventory ON menu.idInventory = inventory.id
                    WHERE menu.date = ?
            """.trimIndent()

            cursor = db.rawQuery(query, arrayOf(date))
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Log.e("LOAD", "Couldn't load data : ${e.message.toString()}")
        }

        var item : Item

        val listItem : ArrayList<Item> = ArrayList()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                item = Item(cursor, Screen.Plan)

                listItem.add(item)

            } while (cursor.moveToNext())
            cursor.close()
        }
        onDataTaken(listItem)
    }

    fun deleteItem(id: Int, isMenu: Boolean){

        try {
            val db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM ${if (isMenu) "menu" else "inventory"} WHERE id=$id")
        }
        catch (e: SQLiteException){
            e.printStackTrace()
            Log.e("DELETE", "Couldn't delete data: ${e.message.toString()}")
        }

    }
}

