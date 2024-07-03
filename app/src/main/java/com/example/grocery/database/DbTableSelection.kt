package com.example.grocery.database

import com.example.grocery.items.Item
import com.example.grocery.items.fromCursorToItem

fun DbManager.itemExists(name: String) : Pair<Long, Boolean> {

    val cursor = this.rawQuery("""SELECT id, COUNT(*) as exist FROM items WHERE name=? """, arrayOf(name))

    var pair: Pair<Long, Boolean> = Pair(-1L, false)

    if (cursor.moveToFirst())
        pair = Pair(
            cursor.getLong(cursor.getColumnIndexOrThrow("id")),
            cursor.getInt(cursor.getColumnIndexOrThrow("exist")) == 1
        )


    cursor.close()
    return Pair(-1L, false)

}

fun DbManager.getAllItems(idPlace: Long) : MutableMap<Long, Item> {


    val listItems : MutableMap<Long, Item> = mutableMapOf()

    val query = """
        SELECT
            *,
            id as idItem
        FROM items
            WHERE idPlace = ?
    """.trimIndent()

    val cursor = this.rawQuery(query, arrayOf(idPlace.toString()))

    while (cursor.moveToNext()){
        val item = fromCursorToItem(cursor)

        listItems[item.id] = item
    }

    cursor.close()
    return listItems
}

fun DbManager.selectShoppingCartInRange(
    startDate: String,
    endDate: String,
    idPlace: Long
) : List<Item> {

    val query = """
        SELECT 
            items.id,
            items.name,
            items.amount_inventory,
            SUM(planning.amount) as amount,
            items.idUnit
        FROM planning 
            INNER JOIN items ON planning.idItem = items.id
        WHERE planning.date>=? and planning.date <=? and idPlace=?
        GROUP BY items.id
    """.trimIndent()

    val cursor = this.rawQuery(query, arrayOf(startDate, endDate, idPlace.toString()))


    val listItems : MutableList<Item> = mutableListOf()

    if (cursor.moveToFirst()) {
        do {
            val item = fromCursorToItem(cursor)

            listItems.add(item)

        } while (cursor.moveToNext())
    }

    cursor.close()
    return listItems

}



fun DbManager.dailyPlan(date: String, idPlace: Long) : MutableMap<Long, MutableMap<Long, Item>> {

    val query = """
            SELECT 
                planning.id,
                planning.idItem,
                items.name,
                planning.amount,
                planning.checked,
                items.idUnit,
                planning.idMoment,
                planning.date,
                items.idPlace
            FROM planning
                INNER JOIN items ON planning.idItem = items.id
            WHERE date=? and idPlace=?
        """.trimIndent()

    val cursor = this.rawQuery(query, arrayOf(date, idPlace.toString()))


    val mapItems : MutableMap<Long, MutableMap<Long, Item>> = mutableMapOf()

    while (cursor.moveToNext()){

        val item = fromCursorToItem(cursor)

        if (!mapItems.containsKey(item.idMoment))
            mapItems[item.idMoment] = mutableMapOf()

        mapItems[item.idMoment]?.set(item.id, item)
    }
    cursor.close()
    return mapItems
}


fun DbManager.getAllMoments(idPlace: Long) : MutableMap<Long, String> {

    val cursor = this.rawQuery("SELECT id, name FROM moments WHERE idPlace=? ", arrayOf(idPlace.toString()))

    val listItems : MutableMap<Long, String> = mutableMapOf()

    if (cursor.moveToFirst()) {
        do {

            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))

            listItems[id] = name

        } while (cursor.moveToNext())
    }
    cursor.close()
    return listItems
}

fun DbManager.getAllUnits(): MutableMap<Long, Pair<String, String>> {
    val cursor = this.rawQuery("SELECT id, name, symbol FROM units", null)

    val mapItems : MutableMap<Long, Pair<String, String>> = mutableMapOf()

    if (cursor.moveToFirst()) {
        do {

            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val symbol = cursor.getString(cursor.getColumnIndexOrThrow("symbol"))

            mapItems[id] = Pair(name, symbol)

        } while (cursor.moveToNext())
    }
    cursor.close()
    return mapItems
}

fun DbManager.getAllPlaces(): MutableMap<Long, String> {
    val cursor = this.rawQuery("SELECT id, name FROM places", null)

    val mapPlace : MutableMap<Long, String> = mutableMapOf()

    if (cursor.moveToFirst()) {
        do {

            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))

            mapPlace[id] = name

        } while (cursor.moveToNext())
    }
    cursor.close()
    return mapPlace
}

fun DbManager.getDefaultIdPlace() : Long{

    val cursor = this.rawQuery("SELECT value FROM info WHERE name=?", arrayOf("defaultIdPlace"))

    if (cursor.moveToFirst())
        return cursor.getString(cursor.getColumnIndexOrThrow("value")).toLong()

    return 0
}