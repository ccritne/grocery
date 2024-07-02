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
        fromGoogleToApp(this)
    }

    fun cleanDb(){
        val db = dbHelper.writableDatabase
        val query = """SELECT name FROM sqlite_master WHERE type='table'"""
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val tableName = cursor.getString(0)
            if(tableName != "sqlite_sequence")
                db.execSQL("DROP TABLE IF EXISTS ${tableName}")
        }

        cursor.close()

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

    fun rawQuery(sql: String, selectionArgs: Array<String>?) : Cursor{
        val db = dbHelper.readableDatabase
        return db.rawQuery(sql, selectionArgs)
    }

    fun delete(table : String, whereClause: String, whereArgs: Array<String>?) : Int {
        val db = dbHelper.writableDatabase
        return db.delete(table, whereClause, whereArgs)
    }


}

