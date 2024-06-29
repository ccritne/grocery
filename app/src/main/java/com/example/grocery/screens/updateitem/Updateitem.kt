package com.example.grocery.screens.updateitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.grocery.App
import com.example.grocery.uielements.date.Date
import com.example.grocery.screens.Screen
import com.example.grocery.uielements.date.getDateNow
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection
import com.example.grocery.manageitems.updateItem


@Composable
fun UpdateItem(
    app: App
) {

    var nameSelector by remember(app.placeSelector) {
        mutableStateOf(
                Pair(app.item.key, app.item.value.name)
        )
    }

    var nameField by remember(app.placeSelector) {
        mutableStateOf(
            if(app.isNewItem.value)
                ""
            else
                app.item.value.name
        )
    }


    val amount = remember {
        mutableIntStateOf(app.item.value.amount)
    }

    val momentSelector = remember(app.placeSelector) {
        mutableLongStateOf(
            if(app.isNewItem.value)
                app.momentsMap.value.entries.first().key
            else
                app.item.value.idMoment
        )
    }


    var unitSymbolSelector by remember(app.placeSelector) {
        mutableStateOf(
            if (app.screen != Screen.Items || (app.screen == Screen.Items && !app.isNewItem.value))
                app.unitsMap.value[app.item.value.idUnit]?.let {
                    Pair(
                        app.item.value.idUnit,
                        it.second
                    )
                }
            else
                Pair(
                    app.unitsMap.value.entries.first().key,
                    app.unitsMap.value.entries.first().value.second
                )
        )
    }



    val date = remember{
        mutableStateOf(
            if (app.screen == Screen.Plan)
                app.dateOperation.value
            else
                getDateNow()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = if(app.screen != Screen.Items) Arrangement.SpaceEvenly else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        if (app.screen == Screen.Items) {
            OutlinedTextField(
                shape = RectangleShape,
                placeholder = { Text(text = "Name", fontSize = 35.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalPizza,
                        contentDescription = "icon"
                    )
                },
                value = nameField,
                onValueChange = { nameField = it },
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .padding(15.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 35.sp)
            )
        }else {
            DropdownMenuSelection(
                list = app.itemsMap.value.map { item -> Pair(item.key, item.value.name) },
                starter = nameSelector
            ) {
                nameSelector = it

                val idUnit = app.itemsMap.value[it.first]?.idUnit
                val symbolUnit = app.unitsMap.value[idUnit]?.second

                if (idUnit != null && symbolUnit != null) {
                    unitSymbolSelector = Pair(
                        idUnit,
                        symbolUnit
                    )
                }
            }

            if (app.screen == Screen.Plan) {
                ChoiceMoment(app = app, momentState = momentSelector){
                    momentSelector.longValue = it
                }
                Date(
                    date = date,
                    enableLeft = true,
                    enableRight = true,
                    modifierIcons = Modifier.size(25.dp),
                    fontSizeText = 35
                ){
                    app.changeDateOperation(it)
                    date.value = it
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (app.screen != Screen.Items) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (app.screen == Screen.ShoppingCart) {
                        Text(text = app.item.value.amountInventory.toString(), fontSize = 35.sp)
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    OutlinedTextField(
                        maxLines = 1,
                        shape = RectangleShape,
                        value = if (amount.intValue != 0) amount.intValue.toString() else "",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        placeholder = {
                            Text(
                                text = "Amount",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        onValueChange = {
                            if (it.isNotEmpty()) {
                                amount.intValue = it.toInt()
                            } else
                                amount.intValue = 0

                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    )
                }
            }
            unitSymbolSelector?.let {
                DropdownMenuSelection(
                    list = app.unitsMap.value.map { unit -> Pair(unit.key, unit.value.second) },
                    enabled = app.screen == Screen.Items,
                    starter = it
                ) {
                    unitSymbolSelector = it
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = { app.navController.navigateUp() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
            IconButton(onClick = {
                updateItem(
                    app = app,
                    screen = app.screen,
                    item = app.item.value,
                    amount = amount.intValue,
                    idUnit = unitSymbolSelector?.first,
                    idMoment = momentSelector.longValue,
                    date = app.dateOperation.value,
                    isNewItem = app.isNewItem.value,
                    nameField = nameField,
                    nameSelector = nameSelector
                )

                app.setItem(app.voidMapEntry)

                app.navController.navigateUp()
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Add item")
            }
        }
    }
}