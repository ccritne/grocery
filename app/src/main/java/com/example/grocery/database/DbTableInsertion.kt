package com.example.grocery.database

import android.content.ContentValues
import com.example.grocery.items.Item


fun DbManager.insertPlace(name: String) : Long{
    val cv = ContentValues()

    cv.put("name", name)

    return this.insert("places", null, cv)
}

fun DbManager.insertMoment(name: String, idPlace: Int) : Long{
    val cv = ContentValues()

    cv.put("name", name)
    cv.put("idPlace", idPlace)

    return this.insert("moments", null, cv)
}

fun DbManager.insertUnit(name: String, symbol: String) : Long{

    val cv = ContentValues()

    cv.put("name", name)
    cv.put("symbol", symbol)

    return this.insert("units", null, cv)
}

fun DbManager.insertPlanItem(item: Item) : Long {

    val cv = ContentValues()

    cv.put("idItem", item.idItem)
    cv.put("amount", item.amount)
    cv.put("idMoment", item.idMoment)
    cv.put("date", item.date)
    cv.put("checked", false)


    return this.insert("planning", null, cv)

}


fun DbManager.insertItemIntoList(item: Item) : Long{
    val cv = ContentValues()

    cv.put("idParent", item.idParent)
    cv.put("name", item.name)
    cv.put("idUnit", item.idUnit)
    cv.put("idPlace", item.idPlace)

    return this.insert("items", null, cv)
}
