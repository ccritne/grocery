package com.example.grocery.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log

fun createMomentsTableIfNotExists(db: SQLiteDatabase){
    try {

        val queryCreation = """CREATE TABLE IF NOT EXISTS moments (
                        id              INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name            TEXT NOT NULL,
                        idPlace         INTEGER NOT NULL 
                                        REFERENCES places (id),
                        dismissed       INTEGER(1) NOT NULL DEFAULT(0)
                    );"""

        db.execSQL(queryCreation)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
    }

    try {
        val queryInsertion = """INSERT INTO moments(name, idPlace) 
            VALUES
                ("Breakfast", 1),
                ("Lunch", 1),
                ("Dinner", 1),
                ("Cleanings", 2),
                ("Morning", 3),
                ("Night", 3);""".trimMargin()

        db.execSQL(queryInsertion)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("INSERT", "Couldn't insert default moments : ${e.message.toString()}")
    }
}

fun createUnitsTableIfNotExists(db: SQLiteDatabase){
    try {

        val queryCreation = """CREATE TABLE IF NOT EXISTS units (
                        id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name        TEXT NOT NULL,
                        symbol      TEXT NOT NULL,
                        dismissed   INTEGER(1) NOT NULL DEFAULT(0)
                    );"""

        db.execSQL(queryCreation)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
    }

    try {
        val queryInsertion = """INSERT INTO units(name, symbol) 
            VALUES
                ("Grams", "g"),
                ("Pieces", "pz"),
                ("Liters", "l"),
                ("Milliliters", "ml")
            ;""".trimMargin()

        db.execSQL(queryInsertion)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("INSERT", "Couldn't insert default units : ${e.message.toString()}")
    }
}

fun createPlaceTableIfNotExists(db: SQLiteDatabase){
    try {

        val queryCreation = """CREATE TABLE IF NOT EXISTS places (
                        id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name        TEXT NOT NULL,
                        dismissed   INTEGER(1) NOT NULL DEFAULT(0)
                    );"""

        db.execSQL(queryCreation)


    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
    }

    try {
        val queryInsertion = """INSERT INTO places(name, dismissed) 
            VALUES
                ("Sideboard", 0),
                ("Home", 0),
                ("Outfits", 0);""".trimMargin()

        db.execSQL(queryInsertion)
    }catch (e: SQLiteException){
        e.printStackTrace()
        Log.e("INSERT", "Couldn't insert default places : ${e.message.toString()}")

    }
}

fun createPlanTableIfNotExists(db: SQLiteDatabase){
    try {

        val query = """CREATE TABLE IF NOT EXISTS planning (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT
                                                   NOT NULL,
                        idItem         INTEGER     NOT NULL
                                        REFERENCES items (id),
                        amount         INTEGER     NOT NULL,
                        idMoment       INTEGER     NOT NULL 
                                        REFERENCES moments (id),
                        date           TEXT(10)    NOT NULL,
                        checked        INTEGER(1)  NOT NULL DEFAULT(0)
                    );"""

        db.execSQL(query)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
    }
}

fun createItemsTableIfNotExists(db: SQLiteDatabase){
    try {

        val query = """CREATE TABLE IF NOT EXISTS items (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT NOT NULL,
                        idParent       INTEGER     NOT NULL DEFAULT(-1),
                        name           TEXT        NOT NULL,
                        price          REAL        NOT NULL DEFAULT(-1.0),
                        idUnit         INTEGER     NOT NULL REFERENCES units (id),
                        idPlace        INTEGER     NOT NULL REFERENCES places (id),
                        
                        dismissed      INTEGER(1)  NOT NULL DEFAULT(0)
                    );"""

        db.execSQL(query)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
    }

    try {
        val queryInsertion = """INSERT INTO items(name, idUnit, idPlace) 
            VALUES
                ("TestItem1", 1, 1),
                ("TestItem2", 1, 2),
                ("TestItem3", 1, 3)
            ;""".trimMargin()

        db.execSQL(queryInsertion)
    }catch (e: SQLiteException){
        e.printStackTrace()
        Log.e("INSERT", "Couldn't insert default places : ${e.message.toString()}")

    }
}

fun createInfoTableIfNotExists(db: SQLiteDatabase){
    try {

        val query = """CREATE TABLE IF NOT EXISTS info (
                        id             INTEGER     PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name           TEXT        NOT NULL,
                        value          INTEGER     NOT NULL
                    );"""

        db.execSQL(query)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Log.e("CREATE", "Couldn't create : ${e.message.toString()}")
    }

    try {
        val queryInsertion = """INSERT INTO info(name, value) 
            VALUES
                ("defaultIdPlace", "1"); """.trimMargin()

        db.execSQL(queryInsertion)
    }catch (e: SQLiteException){
        e.printStackTrace()
        Log.e("INSERT", "Couldn't insert default info : ${e.message.toString()}")

    }


}