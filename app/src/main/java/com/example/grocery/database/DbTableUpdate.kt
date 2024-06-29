package com.example.grocery.database

import android.content.ContentValues
import com.example.grocery.items.Item

fun DbManager.updateInventoryItem(idItem: Long, amount: Int) : Int{
    val cv = ContentValues()

    cv.put("amount", amount)

    return this.update("inventory", cv, "idItem=?", arrayOf(idItem.toString()))
}

fun DbManager.updatePlanItem(item: Item) : Int {

    val cv = ContentValues()

    cv.put("idItem", item.idItem)
    cv.put("amount", item.amount)
    cv.put("idMoment", item.idMoment)
    cv.put("date", item.date)

    return this.update("planning", cv, "id = ?", arrayOf(item.id.toString()))
}

fun DbManager.updatePlanChecked(id: Long, check: Boolean) : Int{

    val cv = ContentValues()
    cv.put("checked",  check)

    return this.update("planning", cv, "id = ?", arrayOf(id.toString()))

}

fun DbManager.updateItemOfList(item: Item) : Int{
    val cv = ContentValues()


    cv.put("idParent", item.idParent)
    cv.put("name", item.name)
    cv.put("price", item.price)
    cv.put("idUnit", item.idUnit)
    cv.put("idPlace", item.idPlace)

    return this.update("items", cv, "id = ?", arrayOf(item.idItem.toString()))
}

fun DbManager.updateDefaultIdPlace(idPlace: Long) : Int{
    val cv = ContentValues()

    cv.put("value", idPlace.toString())

    return this.update("info", cv, "name=?", arrayOf("defaultIdPlace"))
}