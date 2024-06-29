package com.example.grocery.screens.updateitem.ui.constelements.unit

import androidx.compose.runtime.Composable
import com.example.grocery.screens.updateitem.UpdateItem
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UnitSelectorUpdateItem(
    updateItem: UpdateItem
){

    DropdownMenuSelection(
        enabled = updateItem.forSetup,
        list = updateItem.unitsMap.map { item -> Pair(item.key, item.value.second) },
        starter = updateItem.unit,
        onChange = {
            if (updateItem.forSetup)
                updateItem.setValues(idUnit = it.first)
        }
    )

}