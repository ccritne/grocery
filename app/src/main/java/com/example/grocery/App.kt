package com.example.grocery

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.grocery.contentview.ContentView
import com.example.grocery.database.DbManager
import com.example.grocery.database.dailyPlan
import com.example.grocery.database.getAllItems
import com.example.grocery.database.getAllMoments
import com.example.grocery.database.getAllPlaces
import com.example.grocery.database.getAllUnits
import com.example.grocery.database.getDefaultIdPlace
import com.example.grocery.items.Item
import com.example.grocery.screens.Screen
import com.example.grocery.uielements.date.getDateNow
import com.example.grocery.uielements.date.getUpdateDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class App: ComponentActivity() {

    lateinit var dbManager : DbManager
        private set

    val formatterSql: SimpleDateFormat = SimpleDateFormat("y/MM/dd", Locale.getDefault())
    var dateOperation = mutableStateOf(getDateNow())
        private set


    var startDateOperation = mutableStateOf(getDateNow())

    var endDateOperation = mutableStateOf(getUpdateDate(getDateNow(), 6))

    var item = mutableStateOf(Item())
        private set

    var placeSelector by mutableStateOf(Pair(-1L, ""))
        private set

    var placesMap : MutableState<Map<Long, String>> = mutableStateOf(mapOf())
        private set
    private var placesMutableMap = mutableMapOf<Long, String>()

    var momentsMap : MutableState<Map<Long, String>> = mutableStateOf(mapOf())
        private set
    private var momentsMutableMap = mutableMapOf<Long, String>()

    var unitsMap : MutableState<Map<Long, Pair<String, String>>> = mutableStateOf(mapOf())
        private set
    private var unitsMutableMap = mutableMapOf<Long, Pair<String, String>>()

    var dailyPlanMap = mutableStateMapOf<Long, SnapshotStateMap<Long, Item>>()
        private set

    var itemsMap = mutableStateMapOf<Long, Item>()
        private set

    var isNewItem = mutableStateOf(false)
        private set

    var copied : MutableList<Item> = mutableListOf()

    var screen : Screen = Screen.Plan

    lateinit var navController: NavHostController
        private set

    private fun updatePlacesMap(){
        placesMap.value = placesMutableMap
    }

    fun setItemState(state: Boolean){
        isNewItem.value = state
    }


    fun deletePlaces(idPlaces: List<Long>){

        idPlaces.forEach{ idPlace ->
            if(idPlace > 3)
                placesMutableMap.remove(idPlace)
        }

        updatePlacesMap()
    }

    fun addOrUpdatePlaces(idPlace: Long, name: String){
        if(idPlace > 3)
            placesMutableMap[idPlace] = name

        updatePlacesMap()
    }

    fun updatePlaceSelector(newPlace: Pair<Long, String>) {

        placeSelector = newPlace

        updateMaps()
    }

    fun changeDateOperation(newDate: Date){

        dateOperation.value = newDate

        dailyPlanMap = dbManager.dailyPlan(
            date = formatterSql.format(newDate),
            idPlace = placeSelector.first
        )

    }

    fun deleteItemsFromDailyPlan(idsMomentItems: List<Pair<Long, Long>>) {

        idsMomentItems.forEach{ ids ->
            dailyPlanMap[ids.first]?.remove(ids.second)
            if (dailyPlanMap[ids.first]?.isEmpty() == true)
                dailyPlanMap.remove(ids.first)
        }

    }

    fun addOrUpdateItemInPlan(item: Item, oldMoment: Long = -1L){

        Log.i("item - moment", "$item $oldMoment")

        if (oldMoment != -1L)
            dailyPlanMap[oldMoment]?.remove(item.idItem)

        if (!dailyPlanMap.containsKey(item.idMoment))
            dailyPlanMap[item.idMoment] = mutableStateMapOf()


        dailyPlanMap[item.idMoment]?.set(item.idItem, item)


    }


    fun deleteItemsFromList(idItems: List<Long>){
        idItems.forEach { idItem ->
            itemsMap.remove(idItem)
        }
    }

    fun addOrUpdateItemInList(item: Item){

        itemsMap[item.idItem] = item

    }

    private fun updateUnitsMap(){
        unitsMap.value = unitsMutableMap
    }

    fun addOrUpdateUnit(idUnit: Long, name: String, symbol: String){
        if (idUnit > 2)
            unitsMutableMap[idUnit] = Pair(name, symbol)

        updateUnitsMap()
    }

    fun deleteUnits(idUnits: List<Long>){

        idUnits.forEach{ idUnit ->
            if(idUnit > 2)
                unitsMutableMap.remove(idUnit)
        }

        updateUnitsMap()
    }

    private fun updateMomentsMap(){
        momentsMap.value = momentsMutableMap
    }

    fun addOrUpdateMoment(idMoment: Long, name: String){

        if (idMoment > 6)
            momentsMutableMap[idMoment] = name

        updateMomentsMap()
    }

    fun deleteMoments(idMoments: List<Long>){
        idMoments.forEach{ idMoment ->
            if (idMoment > 6)
            momentsMutableMap.remove(idMoment)
        }

        updateMomentsMap()
    }


    fun setTempItem(item: Item){
        this.item.value = item
    }

    fun updateMaps(){

        momentsMutableMap = dbManager.getAllMoments(placeSelector.first)
        updateMomentsMap()

        dailyPlanMap = dbManager.dailyPlan(
            date = formatterSql.format(dateOperation.value),
            idPlace = placeSelector.first
        )

        itemsMap = dbManager.getAllItems(placeSelector.first)


        if (itemsMap.isNotEmpty())
            setTempItem(itemsMap.entries.first().value.copy())


    }

    private fun setupValues(){
        dbManager = DbManager(this as Context)

        unitsMutableMap = dbManager.getAllUnits()
        updateUnitsMap()

        placesMutableMap = dbManager.getAllPlaces()
        updatePlacesMap()

        val defaultIdPlace = dbManager.getDefaultIdPlace()

        updatePlaceSelector(Pair(defaultIdPlace, placesMutableMap[defaultIdPlace]?: ""))

        updateMaps()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupValues()


        setContent {

            navController = rememberNavController()

            ContentView(app = this)

        }
    }
}

