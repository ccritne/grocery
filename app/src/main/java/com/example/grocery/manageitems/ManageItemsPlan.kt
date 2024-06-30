package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updatePlanItem
import com.example.grocery.screens.updateitem.UpdateItem

fun updateItemsPlan(
    app: App,
    updateItem: UpdateItem
){

    val oldMoment = updateItem.item.idMoment
    val item = updateItem.toItem(app.placeSelector.first)

    Log.i("moments", "$oldMoment -> ${item.idMoment}")

    if (updateItem.isNewItem) {
        val id = app.dbManager.insertPlanItem(item)
        item.update(id = id)
    }
    else
        app.dbManager.updatePlanItem(item)

    if (item.id != -1L)
        app.addOrUpdateItemInPlan(item, oldMoment)

}