package com.example.grocery.uielements.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Full width rectangle centered outlinedTextField
@Composable
fun NoLineWidthTextField(
    initialValue : String,
    labelText: String,
    fontSize: TextUnit,
    onValueChange : (String) -> Unit,
){

    var textField by remember {
        mutableStateOf(initialValue)
    }

    OutlinedTextField(
        shape = RectangleShape,
        label = { Text(text = labelText, fontSize = fontSize) },
        value = textField,
        onValueChange = {
            textField = it
            onValueChange(it)
        },
        modifier = Modifier.fillMaxWidth(0.90f),
        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 35.sp)
    )
}