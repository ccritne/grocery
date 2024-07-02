package com.example.grocery.screens.updateitem.ui.setup.name

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.sp
import com.example.grocery.uielements.textfield.NoLineWidthTextField

@Composable
fun UiNameListItems(
    starter: String,
    onChange: (String) -> Unit
){

    NoLineWidthTextField(
        initialValue = starter,
        labelText = "Name",
        fontSize = 25.sp,
        onValueChange = onChange
    )
}