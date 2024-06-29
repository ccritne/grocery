package com.example.grocery.manageitems

import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.utilities.Screen
import java.util.Date

fun updateItem(
    app: App,
    screen: Screen,
    item: Item,
    isNewItem: Boolean,
    nameSelector : Pair<Long, String>,
    nameField: String,
    amount: Int,
    date: Date,
    idUnit: Long?,
    idMoment: Long,
){

    item.update(
        name = if(screen != Screen.Items) nameSelector.second else nameField,
        amount = amount,
        idUnit = idUnit,
    )

    if (screen == Screen.Plan) {
        item.update(idItem = nameSelector.first)

        updateItemsPlan(
            app = app,
            item = item,
            isNewItem = isNewItem,
            date = date,
            idMoment = idMoment
        )
    }

//    if (screen == Screen.Inventory) {
//
//        if (isNewItem) {
//            val id = dbManager.insertItemIntoInventory(item)
//            item.update(id=id)
//        }
//        else
//            dbManager.updateInventoryItem(item.idItem, amount)
//
//        if (item.id != -1L)
//
//    }
//
//    if (screen == Screen.ShoppingCart)
//        dbManager.updateInventoryItem(updatedItem.idItem, amount+updatedItem.amountInventory)
//
//    if (screen == Screen.Items){
//
//        updatedItem.update(idUnit = idUnit)
//
//        if (isNewItem) {
//            val id = dbManager.insertItemIntoList(item = updatedItem)
//            updatedItem.update(id = id)
//        }
//        else
//            dbManager.updateItemOfList(item = updatedItem)
//
//        if (updatedItem.id != -1L)
//            onUpdate(updatedItem)
//    }

}