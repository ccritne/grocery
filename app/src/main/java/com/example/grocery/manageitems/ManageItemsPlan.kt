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

    val oldMoment = updateItem.moment.first
    val item = updateItem.toItem(app.placeSelector.first)

    if (updateItem.isNewItem) {
        val id = app.dbManager.insertPlanItem(item)
        item.update(id = id)
    }
    else
        app.dbManager.updatePlanItem(item)

    Log.i("NEWITEM ID", item.toString()+" "+item.id.toString())

    if (item.id != -1L)
        app.addOrUpdateItemInPlan(item, oldMoment)
}