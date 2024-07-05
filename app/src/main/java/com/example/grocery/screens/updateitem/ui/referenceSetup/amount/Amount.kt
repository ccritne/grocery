package com.example.grocery.screens.updateitem.ui.referenceSetup.amount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

@Composable
fun UiAmountReference(
    starter: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    onChangeAmount: (Int) -> Unit
){
    var starterInt by remember {
        mutableIntStateOf(starter)
    }

    OutlinedTextField(
        modifier = modifier,
        singleLine = true,
        shape = RectangleShape,
        value = if (starterInt != 0) starterInt.toString() else "",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = fontSize),
        onValueChange = {

            if (it.isNotEmpty() && it.isDigitsOnly()) {
                starterInt = it.toInt()
                onChangeAmount(it.toInt())
            }else {
                starterInt = 0
                onChangeAmount(0)
            }
        },
    )
}