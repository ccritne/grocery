package com.example.grocery.database

import com.example.grocery.items.Item

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


    val mapItems : MutableMap<Long, Item> = mutableMapOf()

    val query = """
        SELECT
            items.id,
            items.name,
            items.price,
            items.idUnit,
            items.idPlace
        FROM items
        WHERE idPlace = ?
    """.trimIndent()

    val cursor = this.rawQuery(query, arrayOf(idPlace.toString()))

    while (cursor.moveToNext()){
        val item = Item(cursor)

        mapItems[item.id] = item
    }

    cursor.close()
    return mapItems
}

fun DbManager.selectInventoryItems(idPlace: Long) : MutableMap<Long, Item>{
    val query = """
            SELECT 
                inventory.id,
                items.id as idItem,
                items.name,
                items.idUnit,
                inventory.amount,
                inventory.amount as amountInventory
            FROM inventory
                INNER JOIN items ON inventory.idItem = items.id
            WHERE items.idPlace = ? AND inventory.amount > 0
        """.trimIndent()

    val cursor = this.rawQuery(query, arrayOf(idPlace.toString()))


    val listItem : MutableMap<Long, Item> = mutableMapOf()

    if (cursor.moveToFirst()) {
        do {

            val item = Item(cursor)

            listItem[item.id] = item

        } while (cursor.moveToNext())
    }

    cursor.close()

    return listItem
}

fun DbManager.selectShoppingCartInRange(
    startDate: String,
    endDate: String,
    idPlace: Long
) : Map<Long, Item> {

    val query = """
        SELECT 
            planning.id,
            planning.idItem, 
            items.name, 
            inventory.amount as amountInventory,
            SUM(planning.amount) as amount,
            items.idUnit
        FROM planning 
            INNER JOIN inventory ON planning.idItem = inventory.idItem
            INNER JOIN items ON planning.idItem = items.id
            INNER JOIN units ON items.idUnit = units.id
        WHERE planning.date>=? and planning.date <=? and items.idPlace = ?
        GROUP BY items.name
    """.trimIndent()

    val cursor = this.rawQuery(query, arrayOf(startDate, endDate, idPlace.toString()))

    var item: Item

    val listItems : MutableMap<Long, Item> = mutableMapOf()

    if (cursor.moveToFirst()) {
        do {
            item = Item(cursor)

            listItems[item.idItem] = item

        } while (cursor.moveToNext())
    }

    cursor.close()
    return listItems

}



fun DbManager.dailyPlan(date: String, idPlace: Long) : MutableMap<Long, MutableMap<Long, Item>> {

    val query = """
            SELECT 
                planning.id as id,
                items.id as idItem,
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


    val listItems : MutableMap<Long, MutableMap<Long, Item>> = mutableMapOf()

    while (cursor.moveToNext()){

        val item = Item(cursor)

        if (!listItems.containsKey(item.idMoment))
            listItems[item.idMoment] = mutableMapOf()

        listItems[item.idMoment]?.set(item.id, item)
    }
    cursor.close()
    return listItems
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