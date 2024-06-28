package com.example.grocery.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.grocery.App
import com.example.grocery.body.menu.Date
import com.example.grocery.database.selectShoppingCartInRange
import com.example.grocery.utilities.Screen
import com.example.grocery.utilities.getDateNow


@Composable
fun DialogShopping(
    defaultValue: Int,
    actionOnClosure: (Int, Boolean?) -> Unit
){

    var textValue by remember {
        mutableStateOf(defaultValue.toString())
    }

    Dialog(
        onDismissRequest = { actionOnClosure(0, null) },
    ){
    Column(
        modifier = Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        TextField(
            label = { Text(text = "Change the value", fontSize = 15.sp) },
            modifier = Modifier.padding(15.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            value = textValue, onValueChange = {
                textValue = if(it.isNotEmpty() && it.toInt() >= 0)
                    it.toInt().toString()
                else
                    defaultValue.toString()
        } )

        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = { actionOnClosure(0, false) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close dialog")
            }
            IconButton(onClick = {
                actionOnClosure(textValue.toInt(), true)
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Open dialog")
            }
        }
    }
    }

}



@Composable
fun ShoppingCart(app: App) {

    app.screen = Screen.ShoppingCart

    app.isNewItem.value = false

    val shoppingCart by remember(app.startDateOperation, app.endDateOperation, app.placeSelector) {
        derivedStateOf {
            app.dbManager.selectShoppingCartInRange(
                startDate = app.formatterSql.format(app.startDateOperation.value),
                endDate = app.formatterSql.format(app.endDateOperation.value),
                idPlace = app.placeSelector.first
            )
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Date(
                modifier = Modifier.fillMaxWidth(0.5f),
                date = app.startDateOperation,
                enableLeft = app.startDateOperation.value.after(getDateNow()),
                enableRight = app.startDateOperation.value.before(app.endDateOperation.value),
                modifierIcons = Modifier.size(15.dp),
                fontSizeText = 20
            ){
                app.startDateOperation.value = it
            }

            Date(
                modifier = Modifier.fillMaxWidth(),
                date = app.endDateOperation,
                enableLeft = app.endDateOperation.value.after(app.startDateOperation.value),
                enableRight = true,
                modifierIcons = Modifier.size(15.dp),
                fontSizeText = 20
            ){
                app.endDateOperation.value = it
            }
        }

        if (shoppingCart.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(0.95f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                shoppingCart.forEach {
                    ItemUI(app = app, item = it)
                }
            }
        } else {
            Text(text = "No shopping cart!")
        }
    }

}