package com.example.grocery.uielements.dynamicform

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.items.NeedItem
import com.example.grocery.screens.updateitem.ui.referenceSetup.amount.UiAmountReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.name.UiNameReference
import com.example.grocery.uielements.dropdownmenu.DropdownMenuSelection
import java.util.Collections.addAll
import kotlin.random.Random

@Composable
fun DynamicChildrenForm(
    app: App,
    modifier: Modifier = Modifier,
    starter: List<NeedItem>,
    onAddChildren: (NeedItem) -> Unit,
    onDeleteChildren: (Int) -> Unit,
    onChangeChildren: (Int, NeedItem) -> Unit
){

    val list = remember {
        mutableStateListOf<NeedItem>().apply { addAll(starter) }
    }



    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Needs (for 100g): ", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(16.dp))

        list.forEachIndexed { index, field ->

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownMenuSelection(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    enabled = true,
                    list = app.itemsMap.value.map { item -> Pair(item.key, item.value.name) },
                    starter = Pair(field.id, app.itemsMap.value[field.id]?.name),
                    onChange = {
                        val copyField = field.copy(id = it.first)
                        list[index] = copyField
                        onChangeChildren(index, copyField)
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.size(width = 150.dp, height = 65.dp),
                    singleLine = true,
                    shape = RectangleShape,
                    value = if(field.amount != 0) field.amount.toString() else "",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 25.sp),
                    onValueChange = {

                        var newAmount = 0

                        if (it.isNotEmpty() && it.isDigitsOnly())
                            newAmount = it.toInt()


                        val copyField = field.copy(amount = newAmount)
                        list[index] = copyField
                        onChangeChildren(index, copyField)
                    },
                )
                DropdownMenuSelection(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    enabled = false,
                    list = app.unitsMap.value.map { item -> Pair(item.key, item.value.second) },
                    starter = Pair(app.itemsMap.value[field.id]!!.idUnit, app.unitsMap.value[app.itemsMap.value[field.id]!!.idUnit]?.second),
                    onChange = { }
                )

                // Delete button
                IconButton(onClick = {
                    list.removeAt(index)
                    onDeleteChildren(index)
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
                
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }

        // The size of children list can be max itemsMap.size - actual item
        if (list.size < app.itemsMap.value.size ) {
            Button(onClick = {
                    val newItem = NeedItem(id = app.itemsMap.value.entries.first().value.idItem, amount = 0)
                    list.add(newItem)
                    onAddChildren(newItem)
            }) {
                Text("Add item")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}


@Composable
fun FailedTry(app: App, field: Map.Entry<Long, Pair<String?, Int>>, fieldsView: MutableMap<Long, Pair<String?, Int>>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${field.key}° ingredient: ")
        UiNameReference(
            modifier = Modifier.fillMaxWidth(0.5f),
            itemsMap = app.itemsMap.value, starter = Pair(field.key, field.value.first)
        ) {
            val copyField = field.value.copy(first = app.itemsMap.value[it]?.name)
            fieldsView.remove(field.key)
            fieldsView[it] = copyField
        }
        UiAmountReference(
            fontSize = 24.sp,
            modifier = Modifier.size(height = 65.dp, width = 100.dp),
            starter = field.value.second
        ) {
            fieldsView[field.key] = field.value.copy(second = it)
        }
        IconButton(onClick = {

            Log.i("DELETE", "DELETE ${field.key}")

            fieldsView.remove(field.key)


        }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
    Spacer(modifier = Modifier
        .height(16.dp)
        .fillMaxWidth())
}
