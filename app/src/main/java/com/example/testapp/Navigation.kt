package com.example.testapp

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.body.House
import com.example.testapp.body.ShoppingCart
import com.example.testapp.body.menu.Menu

enum class Screen(){
    House,
    Menu,
    ShoppingCart
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(app: MainActivity, navController: NavHostController, context: Context, dbManager: DbManager){

    NavHost(navController = navController, startDestination = Screen.Menu.name){
        composable(route = Screen.House.name){
            House(app, dbManager)
        }
        composable(route = Screen.Menu.name){
            Menu(app, context, dbManager)
        }
        composable(route = Screen.ShoppingCart.name){
            ShoppingCart(app, context, dbManager)
        }
    }

}