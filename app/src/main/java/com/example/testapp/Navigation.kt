package com.example.testapp

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testapp.body.House
import com.example.testapp.body.menu.Menu
import com.example.testapp.body.ShoppingCart

enum class Screen(){
    House,
    Menu,
    ShoppingCart
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController, context: Context){

    NavHost(navController = navController, startDestination = Screen.Menu.name){
        composable(route = Screen.House.name){
            House()
        }
        composable(route = Screen.Menu.name){
            Menu(context)
        }
        composable(route = Screen.ShoppingCart.name){
            ShoppingCart()
        }
    }

}