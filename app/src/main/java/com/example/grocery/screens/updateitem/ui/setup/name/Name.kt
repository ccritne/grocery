package com.example.grocery.screens.updateitem.ui.setup.name

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.sp
import com.example.grocery.items.Item
import com.example.grocery.items.MutableItem
import com.example.grocery.uielements.textfield.NoLineWidthTextField

@Composable
fun UiNameListItems(
    name: MutableState<String>
){

    NoLineWidthTextField(
        initialValue = name.value,
        labelText = "Name",
        fontSize = 25.sp
    ) {
        name.value = it
    }
}