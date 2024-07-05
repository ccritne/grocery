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
    var localCopy = updatedItem.copy()
    var idMoment = -1L
    if(app.isNewItem.value) {
        val id = app.dbManager.insertPlanItem(updatedItem)
        localCopy = updatedItem.copy(id = id)
    }
    else {
        app.dbManager.updatePlanItem(localCopy)
        idMoment = app.item.value.idMoment
    }


    app.addOrUpdateItemInPlan(localCopy, idMoment)

}