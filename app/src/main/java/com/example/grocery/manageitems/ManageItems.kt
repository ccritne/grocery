package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.screens.Screen

fun updateItem(
    app: App,
    updatedItem: Item
){

    if (app.screen == Screen.Plan ) {

        updateItemsPlan(
            app = app,
            updatedItem = updatedItem
        )
    }

    if (app.screen == Screen.Items || app.screen == Screen.CompositeItems){

        updateItemsList(
            app = app,
            updatedItem = updatedItem
        )
    }

    if (app.screen == Screen.ShoppingCart){
        updateItemsShoppingCart(
            app = app,
            updatedItem = updatedItem
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