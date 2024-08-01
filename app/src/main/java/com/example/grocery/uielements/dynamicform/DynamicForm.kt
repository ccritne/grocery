package com.example.grocery.uielements.dynamicform

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.core.text.isDigitsOnly
import com.example.grocery.App
import com.example.grocery.items.NeedItem
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection



@Composable
fun DynamicUnitsForm(
    app: App,
    modifier: Modifier = Modifier,
    starter: List<Pair<Long, Pair<String, String>>>,
    onAddChildren: () -> Unit,
    onDeleteChildren: (Long) -> Unit,
    onChangeChildren: (Long, String, String) -> Unit
){

    val list = remember {
        mutableStateListOf<Pair<Long, Pair<String, String>>>().apply { addAll(starter) }
    }

    var listId = list.map { item -> item.first}

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(text = "Units: ", fontSize = 30.sp)
            if (app.unitsMap.value.filter { item -> item.key !in listId }.isNotEmpty()) {
                IconButton(onClick = {
                    listId = listId + -1L
                    list.add(Pair(-1L,Pair("","")))
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add ingredient")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            list.forEachIndexed { index, field ->

                var nameUnit by remember {
                    mutableStateOf("")
                }

                var symbolUnit by remember {
                    mutableStateOf("")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .size(width = 150.dp, height = 65.dp),
                        singleLine = true,
                        shape = RectangleShape,
                        value = nameUnit,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 25.sp),
                        onValueChange = {
                            nameUnit = it
                            onChangeChildren(list[index].first, nameUnit, symbolUnit)
                        },
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .size(width = 150.dp, height = 65.dp),
                        singleLine = true,
                        shape = RectangleShape,
                        value = symbolUnit,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 25.sp),
                        onValueChange = {
                            symbolUnit = it
                            onChangeChildren(index.toLong(), nameUnit, symbolUnit)
                        },
                    )

                    // Delete button
                    IconButton(onClick = {
                        onDeleteChildren(index.toLong())
                        list.removeAt(index)
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))
            }

        }
        // The size of children list can be max itemsMap.size - actual item


        Spacer(modifier = Modifier.height(16.dp))

    }
}