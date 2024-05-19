package com.example.testapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Global variable
val screen = mutableStateOf("CALENDAR")

@Composable
fun Body(){
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        when (screen.value) {
            "HOUSE" -> House()
            "CALENDAR" -> Calendar()
            "SHOPPING CART" -> ShoppingCart()
            "NEW ITEM" -> NewItem()
            else -> Text(text = "404")
        }
    }
}