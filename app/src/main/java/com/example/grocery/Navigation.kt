package com.example.grocery

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grocery.body.Inventory
import com.example.grocery.body.ListItems
import com.example.grocery.body.ShoppingCart
import com.example.grocery.body.UpdateItem
import com.example.grocery.body.Plan
import com.example.grocery.body.Profile
import com.example.grocery.utilities.Screen


@Composable
fun NavigationGraph(app: App){

    NavHost(navController = app.navController, startDestination = Screen.Plan.name){
        composable(route = Screen.Inventory.name){
            Inventory(app)
        }
        composable(route = Screen.Plan.name){
            Plan(app)
        }
        composable(route = Screen.ShoppingCart.name){
            ShoppingCart(app)
        }
        composable(route = Screen.UpdateItem.name){
            UpdateItem(app)
        }
        composable(route = Screen.Items.name){
            ListItems(app)
        }
        composable(route = Screen.Profile.name){
            Profile(app)
        }
    }

}