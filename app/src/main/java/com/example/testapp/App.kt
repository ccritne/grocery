package com.example.testapp

import android.content.Context
import android.os.Build
import android.os.Bundle
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testapp.utilities.Food
import com.example.testapp.utilities.Screen
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class App: ComponentActivity() {

    lateinit var dbManager : DbManager
        private set

    var dateOperation: String? = null

    var food : Food = Food()
        private set

    var isNewFood: Boolean? = null

    private var foodCollection: MutableList<Food> = mutableListOf()

    lateinit var navController: NavHostController
        private set

    fun getInventoryCollection() : MutableList<Food>{
        return foodCollection
    }

    fun setFood(food: Food){
        this.food = food
    }

    private fun setupValues(){
        dbManager = DbManager(this as Context)
        dbManager.selectInventoryItems{
            foodCollection = it
        }
    }

    @Composable
    fun getPreviousRoute() : Screen? {
        return navController.currentBackStackEntryAsState().value?.destination?.route?.let {
            Screen.valueOf(
                it
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupValues()


        setContent {
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            navController = rememberNavController()


            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        drawerShape = RectangleShape
                    ) {
                        Text("Grocery", modifier = Modifier.padding(16.dp))
                        HorizontalDivider()
                        NavigationDrawerItem(
                            shape = RectangleShape,
                            label = { Text(text = "Recreate db") },
                            selected = false,
                            onClick = {
                                dbManager.recreateDb()
                                dbManager.fillDataWeek()
                                scope.launch {
                                    drawerState.close()
                                }
                                recreate()
                            }
                        )
                    }
                },
            ) {
                Scaffold(

                    topBar = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "House")
                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Calendar"
                                )
                            }
                        }
                    },
                    bottomBar = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(onClick = { navController.navigate(Screen.House.name) }) {
                                Icon(imageVector = Icons.Default.Home, contentDescription = "House")
                            }
                            IconButton(onClick = { navController.navigate(Screen.Menu.name) }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Calendar"
                                )
                            }
                            IconButton(onClick = { navController.navigate(Screen.ShoppingCart.name) }) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "ShoppingCart"
                                )
                            }
                        }
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {

                            NavigationGraph(this@App)
                        }
                    }
                )
            }



        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun main(){
    App()
}