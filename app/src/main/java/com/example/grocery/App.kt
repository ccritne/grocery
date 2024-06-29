package com.example.grocery

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.grocery.database.selectInventoryItems
import com.example.grocery.items.Item
import com.example.grocery.utilities.Screen
import com.example.grocery.date.getDateNow
import com.example.grocery.date.getUpdateDate
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

    lateinit var item : Map.Entry<Long, Item>
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

    var dailyPlanMap : MutableState<Map<Long, Map<Long, Item>>> = mutableStateOf(mapOf())
        private set
    private var dailyPlanMutableMap = mutableMapOf<Long, MutableMap<Long, Item>>()

    var inventoryMap : MutableState<Map<Long, Item>> = mutableStateOf(mapOf())
        private set
    private var inventoryMutableMap = mutableMapOf<Long, Item>()

    var itemsMap : MutableState<Map<Long, Item>> = mutableStateOf(mapOf())
        private set
    private var itemsMutableMap = mutableMapOf<Long, Item>()

    var isNewItem: MutableState<Boolean> = mutableStateOf(false)

    var screen : Screen = Screen.Plan

    val voidMapEntry = object : Map.Entry<Long, Item> {
        override val key: Long
            get() = -1L
        override val value: Item
            get() = Item()
    }

    lateinit var navController: NavHostController
        private set

    private fun updatePlacesMap(){
        placesMap.value = placesMutableMap
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

    private fun updateDailyPlanMap(){
        dailyPlanMap.value = dailyPlanMutableMap
    }

    fun changeDateOperation(newDate: Date){

        dateOperation.value = newDate

        dailyPlanMutableMap = dbManager.dailyPlan(
            date = formatterSql.format(newDate),
            idPlace = placeSelector.first
        )

        updateDailyPlanMap()
    }

    fun deleteItemsFromDailyPlan(idsMomentPlan: List<Pair<Long, Long>>) {

        idsMomentPlan.forEach{ ids ->
            dailyPlanMutableMap[ids.first]?.remove(ids.second)
        }

        updateDailyPlanMap()
    }

    fun addOrUpdateItemInPlan(item: Item, oldMoment: Long = -1){

        if (oldMoment != -1L)
            dailyPlanMutableMap[oldMoment]?.remove(item.id)

        if (!dailyPlanMutableMap.containsKey(item.idMoment))
            dailyPlanMutableMap[item.idMoment] = mutableMapOf()

        dailyPlanMutableMap[item.idMoment]?.set(item.id, item)

        updateDailyPlanMap()
    }

    private fun updateInventoryMap(){
        inventoryMap.value = inventoryMutableMap
    }

    fun deleteItemsFromInventory(idItems: List<Long>){

        idItems.forEach { idItem ->
            inventoryMutableMap.remove(idItem)
        }

        updateInventoryMap()
    }

    fun addOrUpdateItemInInventory(item: Item){

        inventoryMutableMap[item.idItem] = item


        updateInventoryMap()
    }

    private fun updateItemsMap(){
        itemsMap.value = itemsMutableMap
    }

    fun deleteItemsFromList(idItems: List<Long>){
        idItems.forEach { idItem ->
            itemsMutableMap.remove(idItem)
        }

        updateItemsMap()
    }

    fun addOrUpdateItemInList(item: Item){

        itemsMutableMap[item.idItem] = item

        updateItemsMap()
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


    fun setItem(item: Map.Entry<Long, Item>){
        this.item = item
    }

    fun updateMaps(){

        momentsMutableMap = dbManager.getAllMoments(placeSelector.first)
        updateMomentsMap()

        dailyPlanMutableMap = dbManager.dailyPlan(
            date = formatterSql.format(dateOperation.value),
            idPlace = placeSelector.first
        )
        updateDailyPlanMap()

        inventoryMutableMap = dbManager.selectInventoryItems(placeSelector.first)
        updateInventoryMap()

        itemsMutableMap = dbManager.getAllItems(placeSelector.first)
        updateItemsMap()

        if (itemsMutableMap.isNotEmpty())
            setItem(itemsMutableMap.entries.first())
        else
            setItem(voidMapEntry)

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

