package com.example.grocery.screens.updateitem.ui.constelements.unit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UnitSelectorUpdateItem(
    starter: Pair<Long, String?>,
    enabled: Boolean,
    unitsMap: Map<Long, Pair<String, String>>,
    onChange: (Long) -> Unit
){

    DropdownMenuSelection(
        enabled = enabled,
        list = unitsMap.map { item -> Pair(item.key, item.value.second) },
        starter = starter,
        onChange = {
            onChange(it.first)
        }
    )

}