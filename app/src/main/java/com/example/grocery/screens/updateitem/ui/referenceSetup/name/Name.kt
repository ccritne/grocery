package com.example.grocery.screens.updateitem.ui.referenceSetup.name

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.grocery.items.Item
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection

@Composable
fun UiNameReference(
    modifier : Modifier = Modifier,
    itemsMap: Map<Long, Item>,
    starter: Pair<Long, String?>,
    onChange: (Long) -> Unit
){
    var starterDrop by remember {
        mutableStateOf(starter)
    }


    DropdownMenuSelection(
        modifier = modifier,
        enabled = true,
        list = itemsMap.map { item -> Pair(item.key, item.value.name) },
        starter = starterDrop,
        onChange = {
            starterDrop = it
            onChange(it.first)
        }
    )
}