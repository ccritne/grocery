package com.example.grocery.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.grocery.utilities.fromGoogleToApp


class DbHelper(context: Context?) :
    SQLiteOpenHelper(context, "Grocery", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        createPlanTableIfNotExists(db)
        createMomentsTableIfNotExists(db)
        createPlaceTableIfNotExists(db)
        createUnitsTableIfNotExists(db)
        createItemsTableIfNotExists(db)
        createInfoTableIfNotExists(db)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}

class DbManager
    (ctx: Context?) {
    private val dbHelper = DbHelper(ctx)

    fun fillDataWeek(){

        recreateDb()

        val string = "SD2024/07/08;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;Pasta125g;Pumpkin200g;Banana;SM2;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/07/09;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;Pasta50g;Peas80g;JobLunch;Apple;SM2;Bread100g;Chickpeas125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/07/10;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;BroadBeans100g;Spinach400g;Bread150g;Apple;SM2;PizzaTeglia300g;SD2024/07/11;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;Pasta50g;Beans125g;JobLunch;Banana;SM2;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/07/12;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;Pasta80g;Soia130g;Tomatoes160g;Zucchini100g;Carrots100g;Banana;SM2;Flatbreads250g;PestoSauce95g;Mozzarella125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/07/13;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;Pasta125g;Eggs150g;Parmisan25g;Apple;SM2;Pizza250g;Mozzarella125g;SD2024/07/14;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;Pasta125g;TomatoSauce80g;JobLunch;Banana;SM2;Bread250g;Chickpeas125g;Zucchini100g;Iceberg/Batavia50g;Tomatoes100g;"
        val secondString = "SD2024/07/08;SM0;coffee;Biscuits0g;SD2024/07/09;SM0;coffee;Biscuits0g;SM1;Pasta100g;Carrots;Zucchini;SM2;bread0g;Tomatoes0g;SD2024/07/10;SM0;coffee;Biscuits0g;SM1;Pasta0g;Tuna;Tomatoes0g;SM2;Pizza0g;SD2024/07/11;SD2024/07/12;SD2024/07/13;SD2024/07/14;"

        fromGoogleToApp(string+secondString, this)
    }

    fun cleanDb(){
        val db = dbHelper.writableDatabase
        val query = """SELECT name FROM sqlite_master WHERE type='table'"""
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val tableName = cursor.getString(0)
            if(tableName != "sqlite_sequence")
                db.execSQL("DROP TABLE IF EXISTS $tableName")
        }

        cursor.close()

    }

    fun deletePlan(){
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM planning")
    }

    fun recreateDb(){
        cleanDb()

        val db = dbHelper.writableDatabase

        createPlanTableIfNotExists(db)
        createMomentsTableIfNotExists(db)
        createPlaceTableIfNotExists(db)
        createUnitsTableIfNotExists(db)
        createItemsTableIfNotExists(db)
        createInfoTableIfNotExists(db)
    }

    fun update(table: String, values: ContentValues, whereClause: String, whereArgs: Array<String>?) : Int {
        val db = dbHelper.writableDatabase
        return db.update(table, values, whereClause, whereArgs)
    }

    fun insert(table: String, nullColumnHack: String?, values : ContentValues) : Long{
        val db = dbHelper.writableDatabase
        return db.insert(table, nullColumnHack, values)
    }

    fun insertWithOnConflict(table: String, nullColumnHack: String?, initialValues : ContentValues, conflictAlgorithm: Int) : Long{
        val db = dbHelper.writableDatabase
        return db.insertWithOnConflict(table, nullColumnHack, initialValues, conflictAlgorithm)
    }

    fun rawQuery(sql: String, selectionArgs: Array<String>?) : Cursor{
        val db = dbHelper.readableDatabase
        return db.rawQuery(sql, selectionArgs)
    }

    fun delete(table : String, whereClause: String, whereArgs: Array<String>?) : Int {
        val db = dbHelper.writableDatabase
        return db.delete(table, whereClause, whereArgs)
    }


}

