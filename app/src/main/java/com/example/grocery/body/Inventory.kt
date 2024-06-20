package com.example.grocery.body

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.grocery.utilities.Screen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Inventory(app: App){
    app.screen = Screen.Inventory

    val itemCollection = app.dbManager.selectInventoryItems()

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { ButtonAdd(app = app) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (itemCollection.isNotEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    itemCollection.forEach {item ->
                        ItemUI(
                            app = app,
                            item = item,
                        )
                    }
                }
            } else {
                Text(text = "No items found!")
            }
        }
    }
}