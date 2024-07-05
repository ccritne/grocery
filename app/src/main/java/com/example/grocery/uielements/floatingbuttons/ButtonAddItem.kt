package com.example.grocery.uielements.floatingbuttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.screens.Screen

@Composable
fun ButtonAdd(
    app: App
){
    var alertVisible by remember {
        mutableStateOf(false)
    }

    if (alertVisible)
        AlertDialog(
            onDismissRequest = { alertVisible = false },
            confirmButton = { IconButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                alertVisible = false

                app.navController.navigate(Screen.Items.name)
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "go")
            } },
            icon = { Icon(imageVector = Icons.Default.Dangerous, contentDescription = "Alert", tint = Color.Red)},
            title = { Text(modifier = Modifier.fillMaxWidth(), text = "First item".uppercase(), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)},
            text = { Text(modifier = Modifier.fillMaxWidth(), text = "Insert your first item in this place.", fontSize = 24.sp, textAlign = TextAlign.Center)}
        )


    FloatingActionButton(
        shape = CircleShape,
        onClick = {
            if(app.itemsMap.value.isNotEmpty() || app.screen == Screen.Items) {
                app.setItemState(true)
                app.navController.navigate(Screen.UpdateItem.name)
            }else
                alertVisible = true

        }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add item")
    }
}