package com.example.grocery.screens.updateitem.ui.setup.name

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.grocery.screens.updateitem.ui.UpdateItem
import com.example.grocery.uielements.textfield.NoLineWidthTextField

@Composable
fun UiNameList(
    updateItem: UpdateItem
){

    NoLineWidthTextField(
        initialValue = updateItem.name,
        labelText = "Name",
        fontSize = 25.sp
    ) {
        updateItem.setValues(name = it)
    }
}