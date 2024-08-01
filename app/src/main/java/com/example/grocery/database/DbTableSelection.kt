package com.example.grocery.database

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
    return pair

}

fun DbManager.getAllItems(idPlace: Long) : SnapshotStateMap<Long, Item> {


    val listItems : SnapshotStateMap<Long, Item> = mutableStateMapOf()

    val query = """
        SELECT
            *,
            id as idItem
        FROM items
            WHERE idPlace = ?
        ORDER BY UPPER(name) ASC
            
    """.trimIndent()

    val cursor = this.rawQuery(query, arrayOf(idPlace.toString()))

    while (cursor.moveToNext()){
        val item = fromCursorToItem(cursor)

        listItems[item.id] = item
    }

    cursor.close()
    return listItems
}

fun DbManager.checkIfItemWithIdExistsInThisMoment(idItem: Long, idMoment: Long, date: String): Boolean{
    val cursor = this.rawQuery("SELECT count(*) > 0 as itemExist FROM planning WHERE idItem=? AND idMoment=? AND date=? LIMIT 1", arrayOf(idItem.toString(), idMoment.toString(), date))
    cursor.moveToNext()
    return cursor.getInt(cursor.getColumnIndexOrThrow("itemExist")) == 1
}

fun DbManager.checkIfItemWithNameExistsInThisPlace(name: String, idPlace: Long, idItem: Long = -1L) : Boolean{
    val cursor = this.rawQuery("SELECT count(*) > 0 as itemExist FROM items WHERE UPPER(name)=UPPER(?) AND idPlace=? AND id<>? LIMIT 1", arrayOf(name, idPlace.toString(), idItem.toString()))
    cursor.moveToNext()
    return cursor.getInt(cursor.getColumnIndexOrThrow("itemExist")) == 1
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
            items.idUnit,
            items.children
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



fun DbManager.dailyPlan(date: String, idPlace: Long) : SnapshotStateMap<Long, SnapshotStateMap<Long, Item>> {

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


    val mapItems : SnapshotStateMap<Long, SnapshotStateMap<Long, Item>> = mutableStateMapOf()

    while (cursor.moveToNext()){

        val item = fromCursorToItem(cursor)

        if (!mapItems.containsKey(item.idMoment))
            mapItems[item.idMoment] = mutableStateMapOf()

        mapItems[item.idMoment]?.set(item.idItem, item)
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