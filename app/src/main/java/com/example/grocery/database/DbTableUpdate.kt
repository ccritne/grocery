package com.example.grocery.database

import android.content.ContentValues
import android.util.Log
import com.example.grocery.items.Item
import com.example.grocery.screens.updateitem.UpdateItem

fun DbManager.updateShoppingCart(updateItem: UpdateItem, idPlace: Long): Item{

    Log.i("UPDATE SHOPPING CART", updateItem.toItem(idPlace).toString())

    val cursorExist = this.rawQuery("SELECT EXISTS(SELECT 1 FROM inventory WHERE idItem=?) as exist", arrayOf(updateItem.idItem.toString()))

    val item = updateItem.toItem(idPlace)


    if(cursorExist.moveToFirst()) {

        val exist = cursorExist.getInt(cursorExist.getColumnIndexOrThrow("exist"))

        var amountInventory = 0

        if(exist == 0) {


            cursorExist.close()

            this.insertItemIntoInventory(item)
        }else{
            val cursorAmountInventory = this.rawQuery("SELECT IFNULL(amount, 0) as amountInventory FROM inventory WHERE idItem=?", arrayOf(updateItem.idItem.toString()))
            if(cursorAmountInventory.moveToFirst())
                amountInventory = cursorAmountInventory.getInt(cursorAmountInventory.getColumnIndexOrThrow("amountInventory"))
        }

        item.update(amountInventory = updateItem.amount + amountInventory)

        val cv = ContentValues()

        cv.put("amount", item.amountInventory)

        this.update("inventory", cv, "idItem=?", arrayOf(updateItem.idItem.toString()))
    }

    return item
}


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