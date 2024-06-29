package com.example.grocery.screens.updateitem.ui.referenceSetup.name

import androidx.compose.runtime.Composable
import com.example.grocery.screens.updateitem.UpdateItem
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UiNameReference(
    updateItem: UpdateItem,
){

    val starter = if (updateItem.isNewItem)
            Pair(updateItem.itemsMap.entries.first().key, updateItem.itemsMap.entries.first().value.name)
        else
            Pair(updateItem.idItem, updateItem.name)


    DropdownMenuSelection(
        list = updateItem.itemsMap.map { item -> Pair(item.key, item.value.name) },
        starter = starter,
        onChange = {
            updateItem.changeNameFromDropDown(it.first)
        }
    )
}