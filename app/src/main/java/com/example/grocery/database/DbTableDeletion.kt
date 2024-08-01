package com.example.grocery.database

fun DbManager.deleteItem(table: String, id: Long) : Int {

    return this.delete(table,"id=?", arrayOf(id.toString()))

}