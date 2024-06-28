package com.example.grocery.database

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import android.util.Log

fun DbManager.deleteInventoryItem(id: Long){

    this.delete("inventory", "id=?", arrayOf(id.toString()))

}

fun DbManager.deleteItem(table: String, id: Long) : Int {
    if (table == "planning")
        return this.delete("planning", "id=?", arrayOf(id.toString()))

    val cv = ContentValues()
    cv.put("dismissed", true)
    return this.update(table, cv, "id=?", arrayOf(id.toString()))

}