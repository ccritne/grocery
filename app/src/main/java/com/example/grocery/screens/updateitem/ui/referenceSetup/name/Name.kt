package com.example.grocery.screens.updateitem.ui.referenceSetup.name

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.grocery.screens.updateitem.UpdateItem
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UiNameReference(
    updateItem: UpdateItem,
){

    var starter by remember {
        mutableStateOf(
            if (updateItem.isNewItem)
                Pair(
                    updateItem.itemsMap.entries.first().key,
                    updateItem.itemsMap.entries.first().value.name
                )
            else
                Pair(updateItem.idItem, updateItem.name)
        )
    }


    DropdownMenuSelection(
        enabled = true,
        list = updateItem.itemsMap.map { item -> Pair(item.key, item.value.name) },
        starter = starter,
        onChange = {
            starter = it
            updateItem.changeNameFromDropDown(it.first)
        }
    )
}