package com.example.grocery.screens.updateitem.ui.constelements.unit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.grocery.screens.updateitem.UpdateItem
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UnitSelectorUpdateItem(
    updateItem: UpdateItem
){
    val starter by remember{
        mutableStateOf(
            if(updateItem.isNewItem)
                Pair(updateItem.unitsMap.entries.first().key, updateItem.unitsMap.entries.first().value.second)
            else
                Pair(updateItem.idUnit, updateItem.unitsMap[updateItem.idUnit]!!.second)
        )
    }

    DropdownMenuSelection(
        enabled = updateItem.forSetup,
        list = updateItem.unitsMap.map { item -> Pair(item.key, item.value.second) },
        starter = starter,
        onChange = {
            if (updateItem.forSetup)
                updateItem.setValues(idUnit = it.first)
        }
    )

}