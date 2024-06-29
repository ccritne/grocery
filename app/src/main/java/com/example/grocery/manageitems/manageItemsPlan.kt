package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updatePlanItem
import com.example.grocery.utilities.Item
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun updateItemsPlan(
    app: App,
    idMoment: Long,
    date: Date,
    item: Item,
    isNewItem: Boolean
){

    val formatterSql = SimpleDateFormat("y/MM/dd", Locale.getDefault())
    val oldMoment = item.idMoment

    item.update(
        date = formatterSql.format(date),
        idMoment = idMoment
    )

    if (isNewItem) {
        val id = app.dbManager.insertPlanItem(item)
        item.update(id = id)
    }
    else
        app.dbManager.updatePlanItem(item)

    Log.i("NEWITEM ID", isNewItem.toString()+" "+item.id.toString())

    if (item.id != -1L)
        app.addOrUpdateItemInPlan(item, oldMoment)
}