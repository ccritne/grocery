package com.example.grocery.screens.updateitem.ui.referenceSetup.name

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.grocery.items.Item
import com.example.grocery.items.MutableItem
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UiNameReference(
    itemsMap: Map<Long, Item>,
    unitsMap: Map<Long, Pair<String, String>>,
    idItem: MutableLongState,
    nameRef: MutableState<String>,
    starterRef: MutableState<Pair<Long, String>>,
    idUnit: MutableLongState,
    unit: MutableState<String>,
){


    DropdownMenuSelection(
        enabled = true,
        list = itemsMap.map { item -> Pair(item.key, item.value.name) },
        starter = starterRef.value,
        onChange = {
            idItem.longValue = it.first
            nameRef.value = it.second
            starterRef.value = it
            idUnit.longValue = itemsMap[it.first]!!.idUnit
            unit.value = unitsMap[idUnit.longValue]!!.second
        }
    )
}