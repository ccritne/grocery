package com.example.testapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbManager = DbManager(this as Context)
        dbManager.createIfNotExists()


        setContent {

            val navController = rememberNavController()

            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "House" )
                        }
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Calendar" )
                        }
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(onClick = { navController.navigate(Screen.House.name) }) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "House" )
                        }
                        IconButton(onClick = { navController.navigate(Screen.Menu.name) }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar" )
                        }
                        IconButton(onClick = { navController.navigate(Screen.ShoppingCart.name) }) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "ShoppingCart" )
                        }
                    }
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {

                        NavigationGraph(navController, context = this@MainActivity as Context, dbManager)
                    }
                }
            )

            

        }
    }
}