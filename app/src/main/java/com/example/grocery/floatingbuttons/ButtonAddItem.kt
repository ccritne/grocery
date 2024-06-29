package com.example.grocery.floatingbuttons

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.grocery.App
import com.example.grocery.utilities.Screen

@Composable
fun ButtonAdd(
    app: App
){
    FloatingActionButton(
        shape = CircleShape,
        onClick = {
            app.isNewItem.value = true

            if (app.itemsMap.value.isNotEmpty())
                app.setItem(app.itemsMap.value.entries.first())
            else
                app.setItem(app.voidMapEntry)

            app.navController.navigate(Screen.UpdateItem.name)
        }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add item")
    }
}