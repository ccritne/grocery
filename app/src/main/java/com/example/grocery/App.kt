package com.example.grocery

import android.content.Context
import android.icu.util.LocaleData
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.grocery.database.DbManager
import com.example.grocery.utilities.Food
import com.example.grocery.utilities.Screen
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class App: ComponentActivity() {

    lateinit var dbManager : DbManager
        private set

    private val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")


    var dateOperation: String = LocalDate.now().format(formatterSql)

    var food : Food = Food()
        private set

    var isNewFood: MutableState<Boolean> = mutableStateOf(false)

    var screen : Screen = Screen.Menu


    lateinit var navController: NavHostController
        private set

    fun setFood(food: Food){
        this.food = food
    }

    private fun setupValues(){
        dbManager = DbManager(this as Context)

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

