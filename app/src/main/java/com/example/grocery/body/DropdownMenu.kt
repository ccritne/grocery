package com.example.grocery.body

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun <T> DropdownMenuSelection(
    enabled: MutableState<Boolean> = mutableStateOf(true),
    list: List<T>,
    starter: MutableState<T>,
    onChange: (T) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    var selectedText by starter

    Column(
        modifier = Modifier.fillMaxWidth(0.5f)
    ) {
        IconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick =  { if (enabled.value) expanded = true }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedText.toString())
                if (enabled.value) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    enabled = enabled.value,
                    text = {
                        Text(
                            text = item.toString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        selectedText = item
                        expanded = false
                        onChange(item)
                        println("item: "+item)
                    }
                )
            }
        }


    }
}