package com.example.grocery.manageitems

import com.example.grocery.App
import com.example.grocery.screens.Screen
import com.example.grocery.screens.updateitem.UpdateItem

fun updateItem(
    app: App,
    updateItem: UpdateItem
){

    if (app.screen == Screen.Plan) {

        updateItemsPlan(
            app = app,
            updateItem = updateItem
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