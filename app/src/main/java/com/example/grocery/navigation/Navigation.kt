package com.example.grocery.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grocery.App
import com.example.grocery.screens.inventory.Inventory
import com.example.grocery.screens.listitems.ListItems
import com.example.grocery.screens.plan.Plan
import com.example.grocery.screens.userprofile.Profile
import com.example.grocery.screens.shoppingcart.ShoppingCart
import com.example.grocery.screens.updateitem.UpdateItem
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