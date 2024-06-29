package com.example.grocery.screens.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.uielements.floatingbuttons.ButtonAdd
import com.example.grocery.items.ItemUI
import com.example.grocery.utilities.Screen


@Composable
fun Inventory(app: App){
    app.screen = Screen.Inventory

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { ButtonAdd(app = app) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (app.inventoryMap.value.isNotEmpty()) {
                app.inventoryMap.value.forEach { item ->

                    ItemUI(
                        app = app,
                        item = item,
                    )
                }
            } else {
                Text(text = "No items found!")
            }
        }
    }
}