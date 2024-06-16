package com.example.testapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.body.House
import com.example.testapp.body.ShoppingCart
import com.example.testapp.body.UpdateItem
import com.example.testapp.body.menu.Menu
import com.example.testapp.utilities.Screen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(app: App){

    NavHost(navController = app.navController, startDestination = Screen.Menu.name){
        composable(route = Screen.House.name){
            House(app)
        }
        composable(route = Screen.Menu.name){
            Menu(app)
        }
        composable(route = Screen.ShoppingCart.name){
            ShoppingCart(app)
        }
        composable(route = Screen.UpdateItem.name){
            UpdateItem(app)
        }
    }

}