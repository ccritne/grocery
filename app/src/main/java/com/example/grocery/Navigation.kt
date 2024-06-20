package com.example.grocery

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grocery.body.Inventory
import com.example.grocery.body.ShoppingCart
import com.example.grocery.body.UpdateItem
import com.example.grocery.body.menu.Plan
import com.example.grocery.utilities.Screen


@RequiresApi(Build.VERSION_CODES.O)
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
    }

}