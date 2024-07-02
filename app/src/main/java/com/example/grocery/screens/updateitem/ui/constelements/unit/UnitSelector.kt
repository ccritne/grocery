package com.example.grocery.screens.updateitem.ui.constelements.unit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UnitSelectorUpdateItem(
    unitsMap: Map<Long, Pair<String, String>>,
    idUnit: MutableLongState,
    unit: MutableState<String>
){
    var starter by remember{
        mutableStateOf(Pair(idUnit.longValue, unit.value))
    }

    DropdownMenuSelection(
        enabled = true,
        list = unitsMap.map { item -> Pair(item.key, item.value.second) },
        starter = starter,
        onChange = {
            idUnit.longValue = it.first
            unit.value = it.second
            starter = it
        }
    )

}