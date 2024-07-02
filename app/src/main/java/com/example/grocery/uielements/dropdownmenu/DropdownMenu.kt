package com.example.grocery.uielements.dropdownmenu

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun DropdownMenuSelection(
    enabled: Boolean = true,
    list: List<Pair<Long, String>>,
    starter: Pair<Long, String?>,
    onChange: (Pair<Long, String>) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(0.5f)
    ) {
        IconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick =  { if (enabled) expanded = true }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                starter.second?.let { Text(text = it, fontSize = 25.sp) }
                if (enabled) {
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
                    enabled = enabled,
                    text = {
                        Text(
                            text = item.second,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 20.sp
                        )
                    },
                    onClick = {
                        expanded = false
                        onChange(item)
                    }
                )
            }
        }


    }
}