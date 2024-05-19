package com.example.testapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BottomsBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(onClick = { screen.value = "HOUSE" }) {
            Icon(imageVector = Icons.Default.Home, contentDescription = "House" )
        }
        IconButton(onClick = { screen.value = "CALENDAR" }) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar" )
        }
        IconButton(onClick = { screen.value = "SHOPPING CART" }) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "ShoppingCart" )
        }
    }
}