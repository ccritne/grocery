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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UiAmountReference(
    starter: Int,
    modifier: Modifier = Modifier,
    onChangeAmount: (Int) -> Unit
){

    Row(
        modifier = modifier.fillMaxWidth(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            maxLines = 1,
            shape = RectangleShape,
            value = if (starter != 0) starter.toString() else "",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(
                    text = "Amount",
                    modifier = Modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Center
                )
            },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            onValueChange = {


                if (it.isNotEmpty())
                    onChangeAmount(it.toInt())
                else
                    onChangeAmount(0)

            },
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .padding(end = 15.dp)
                .fillMaxHeight(0.1f)
        )
        
    }
}