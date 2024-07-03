package com.example.grocery.uielements.floatingbuttons

import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.grocery.App
import com.example.grocery.screens.Screen

@Composable
fun ButtonAdd(
    app: App
){
    Button(
        shape = CircleShape,
        onClick = {
            app.setItemState(true)
            app.updateItem.value = !app.updateItem.value
            app.navController.navigate(Screen.UpdateItem.name)
        }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add item")
    }
}