package com.example.grocery.database

import android.content.ContentValues
import com.example.grocery.items.Item
import com.example.grocery.utilities.getFormatterDateSql

fun DbManager.updateShoppingCart(item: Item): Int{

    val cv = ContentValues()

    cv.put("amount_inventory", item.amountInventory)

    return this.update("items", cv, "id=?", arrayOf(item.id.toString()))

}



fun DbManager.updatePlanItem(item: Item) : Int {

    val cv = ContentValues()


    cv.put("idItem", item.idItem)
    cv.put("amount", item.amount)
    cv.put("idMoment", item.idMoment)
    cv.put("date", getFormatterDateSql().format(item.date))

    return this.update("planning", cv, "id = ?", arrayOf(item.id.toString()))
}

fun DbManager.updatePlanChecked(id: Long, check: Boolean) : Int{

    val cv = ContentValues()
    cv.put("checked",  check)

    return this.update("planning", cv, "id = ?", arrayOf(id.toString()))

}

fun DbManager.updateItemOfList(item: Item) : Int{
    val cv = ContentValues()


    cv.put("children", item.children.toString())
    cv.put("name", item.name)
    cv.put("amount_inventory", item.amountInventory)
    cv.put("price", item.price)
    cv.put("idUnit", item.idUnit)
    cv.put("idPlace", item.idPlace)

    return this.update("items", cv, "id = ?", arrayOf(item.id.toString()))
}

fun DbManager.updateDefaultIdPlace(idPlace: Long) : Int{
    val cv = ContentValues()

    cv.put("value", idPlace.toString())

    return this.update("info", cv, "name=?", arrayOf("defaultIdPlace"))
}