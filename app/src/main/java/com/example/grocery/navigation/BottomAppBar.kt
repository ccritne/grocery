package com.example.grocery.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.screens.Screen

@Composable
fun BottomAppBar(
    app: App
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            app.navController.navigate(Screen.Plan.name)
        }) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar"
            )
        }
        IconButton(onClick = {
            app.navController.navigate(Screen.ShoppingCart.name)
        }) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "ShoppingCart"
            )
        }
        IconButton(onClick = {
            app.navController.navigate(Screen.Items.name)
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "ListItems"
            )
        }
    }
}