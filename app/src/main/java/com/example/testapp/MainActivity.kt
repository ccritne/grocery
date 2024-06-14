package com.example.testapp

import android.content.Context
import android.icu.util.LocaleData
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testapp.body.fromGoogleToApp
import com.example.testapp.body.menu.Moments
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity() : ComponentActivity() {



    val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")
    var date: LocalDate by mutableStateOf(LocalDate.now())

    var startDate: LocalDate by mutableStateOf(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
    var endDate: LocalDate by mutableStateOf(startDate.plusDays(6))


    private var isAddedItemVisible by mutableStateOf(false)

    private var name by mutableStateOf("")
    private var grams by mutableStateOf("")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbManager = DbManager(this as Context)

        setContent {
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val navController = rememberNavController()


            if(isAddedItemVisible){
                var momentSelector by remember { mutableStateOf(Moments.Breakfast) }

                Dialog(onDismissRequest = { isAddedItemVisible = false }) {
                    Column(
                        modifier = Modifier.background(color = Color.White),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        TextField(
                            label = {
                                Text(
                                    text = "Name:",
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center
                                )
                                    },
                            singleLine = true,
                            value = name,
                            onValueChange = {
                                if (it.isNotEmpty())
                                    name = it
                            },
                            modifier = Modifier.padding(15.dp),
                            textStyle = TextStyle(textAlign = TextAlign.Center)
                        )
                        TextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            label = {
                                Text(
                                    text = "Grams:",
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center
                                )
                                    },
                            singleLine = true,
                            value = grams,
                            onValueChange = { grams = it },
                            modifier = Modifier.padding(15.dp),
                            textStyle = TextStyle(textAlign = TextAlign.Center)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                        ) {
                            Moments.entries.chunked(1).forEach{moments ->
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {

                                    moments.forEach {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = momentSelector == it,
                                                onClick = { momentSelector = it })

                                            Text(text = it.name)
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            IconButton(onClick = { isAddedItemVisible = false }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close dialog")
                            }
                            IconButton(onClick = {
                                dbManager.insertFood(name, grams.toInt(), momentSelector.ordinal, date.format(formatterSql))
                                isAddedItemVisible = false
                            }) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Close dialog")
                            }
                        }
                    }
                }
            }



            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        drawerShape = RectangleShape
                    ) {
                        Text("Grocery", modifier = Modifier.padding(16.dp))
                        Divider()
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
                    floatingActionButton = {
                           if(navController.currentBackStackEntryAsState().value?.destination?.route == Screen.Menu.name){
                               FloatingActionButton(onClick = {
                                    isAddedItemVisible = true
                               }) {
                                   Icon(imageVector = Icons.Default.Add, contentDescription = "Add item")
                               }
                           }
                    },
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

                            NavigationGraph(
                                this@MainActivity,
                                navController,
                                context = applicationContext,
                                dbManager
                            )
                        }
                    }
                )
            }

            

        }
    }
}