package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updatePlanItem
import com.example.grocery.items.Item

fun updateItemsPlan(
    app: App,
    updatedItem: Item
){
    Log.i("oldItem", app.item.toString())
    Log.i("updatedItem", updatedItem.toString())

    var localCopy = updatedItem.copy()
    var idMoment = -1L
    var id = -1L

    if(app.isNewItem.value) {
        id = app.dbManager.insertPlanItem(updatedItem)
        localCopy = updatedItem.copy(id = id)
    }
    else {
        id = app.dbManager.updatePlanItem(localCopy).toLong()
        idMoment = app.item.value.idMoment
    }

    if (id > 0L)
        app.addOrUpdateItemInPlan(localCopy, idMoment)

}