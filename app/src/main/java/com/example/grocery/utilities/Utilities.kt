package com.example.grocery.utilities

import android.database.Cursor
import android.util.Log
import com.example.grocery.database.DbManager
import com.example.grocery.database.insertItemIntoInventory
import com.example.grocery.database.insertItemIntoList
import com.example.grocery.database.insertMoment
import com.example.grocery.database.insertPlace
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.insertUnit
import com.example.grocery.database.itemExists
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun <T, R> fromPairToMapEntry(pair : Pair<T, R>) : Map.Entry<T, R> {
    return object : Map.Entry<T, R> {
        override val key: T
            get() = pair.first
        override val value: R
            get() = pair.second
    }
}

fun getInstance(): Calendar{
    return Calendar.getInstance()
}

fun getDateNow() : Date{
    return getInstance().time
}

fun getUpdateDate(date: Date, days: Int) : Date{
    val instance = Calendar.getInstance()
    instance.time = date
    instance.add(Calendar.DATE, days)
    return instance.time
}

fun getDateTime(simpleDateFormat: SimpleDateFormat) : String {

    val calendarTime = getDateNow()
    val formattedTime = simpleDateFormat.format(calendarTime)

    return formattedTime.toString()

}

fun fromGoogleToApp(dbManager: DbManager){
    val string = "SD2024/06/17;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;Pasta125g;Pumpkin200g;Banana;SM2;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/18;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;Pasta50g;Peas80g;JobLunch;Apple;SM2;Bread100g;Chickpeas125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/19;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;BroadBeans100g;Spinach400g;Bread150g;Apple;SM2;PizzaTeglia300g;SD2024/06/20;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;Pasta50g;Beans125g;JobLunch;Banana;SM2;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/21;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;Pasta80g;Soia130g;Tomatoes160g;Zucchini100g;Carrots100g;Banana;SM2;Flatbreads250g;PestoSauce95g;Mozzarella125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/22;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;Pasta125g;Eggs150g;Parmisan25g;Apple;SM2;Pizza250g;Mozzarella125g;SD2024/06/23;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;Pasta125g;TomatoSauce80g;JobLunch;Banana;SM2;Bread250g;Chickpeas125g;Zucchini100g;Iceberg/Batavia50g;Tomatoes100g"
    val filterByDay = string.split("SD").toMutableList()

    val items: MutableList<Item> = mutableListOf()

    dbManager.recreateDb()

    val idGrams = 1L
    val idPieces = 2L

    val idSideboard = 1L
    val idHome = 2L

    val idBreakfast = 1L
    val idLunch = 2L
    val idDinner = 3L

    val idWasher = dbManager.insertMoment("Washer", idHome.toInt())

    filterByDay.forEach{ listDay->
        if (listDay.isNotEmpty()){
            val date = listDay.subSequence(0 .. 9).toString()
            val moments = listDay.subSequence(11, listDay.length).split("SM")

            moments.forEach{ moment ->
                if(moment.isNotEmpty()){
                    val ingredients = moment.split(";")
                    val momentChoice = when(ingredients[0].toInt()){
                        idBreakfast.toInt() - 1 -> idBreakfast
                        idLunch.toInt() - 1 -> idLunch
                        idDinner.toInt() - 1 -> idDinner
                        else -> -1
                    }
                    val ingredientsPop = ingredients.subList(1, ingredients.size-1)

                    ingredientsPop.forEach{
                            ingredient ->

                        if (ingredient.isNotEmpty()) {
                            var changeIngredient = ingredient

                            var unit = idPieces
                            if(ingredient.last() == 'g' && ingredient[ingredient.length-2].isDigit()) {
                                unit = idGrams
                                changeIngredient =
                                    ingredient.subSequence(0, ingredient.length - 1).toString()
                            }

                            val name = changeIngredient.filter { !it.isDigit() }
                            val amount = changeIngredient.filter { it.isDigit() }

                            val amountInt = if(amount.isNotEmpty()) amount.toInt() else 1

                            val item = Item()

                            item.update(
                                name = name,
                                amount = amountInt,
                                date = date,
                                idMoment = momentChoice,
                                idUnit = unit,
                                idPlace = idSideboard
                            )

                            items.add(item)
                        }

                    }

                }

            }

        }


    }

    val clothItem = Item()
    clothItem.update(
        name = "White T-shirt",
        amount = 1,
        date = string.substring(2, 12),
        idMoment = idWasher,
        idPlace = idHome,
        idUnit = idPieces
    )

    items.add(clothItem)

    items.forEach {
        item ->

        val pair = dbManager.itemExists(item.name)

        val idItem = if(!pair.second)
                dbManager.insertItemIntoList(item)
            else
                pair.first


        item.update(idItem = idItem, amountInventory = 0)

        dbManager.insertPlanItem(item)

        if(!pair.second)
            dbManager.insertItemIntoInventory(item)
    }
}

enum class Screen(){
    Inventory,
    Plan,
    ShoppingCart,
    Items,
    UpdateItem,
    Profile
}


class Item() {

    constructor(cursor: Cursor? = null) : this() {
        if(cursor != null) {
            val idColumnIndex = cursor.getColumnIndex("id")
            if (idColumnIndex != -1)
                id = cursor.getLong(idColumnIndex)

            val idItemColumnIndex = cursor.getColumnIndex("idItem")
            if (idItemColumnIndex != -1)
                idItem = cursor.getLong(idItemColumnIndex)

            val nameColumnIndex = cursor.getColumnIndex("name")
            if (nameColumnIndex != -1)
                name = cursor.getString(nameColumnIndex)

            val amountColumnIndex = cursor.getColumnIndex("amount")
            if (amountColumnIndex != -1)
                amount = cursor.getInt(amountColumnIndex)

            val amountInventoryColumnIndex = cursor.getColumnIndex("amountInventory")
            if (amountInventoryColumnIndex != -1)
                amountInventory = cursor.getInt(amountInventoryColumnIndex)

            val dateColumnIndex = cursor.getColumnIndex("date")
            if (dateColumnIndex != -1)
                date = cursor.getString(dateColumnIndex)

            val priceColumnIndex = cursor.getColumnIndex("price")
            if (priceColumnIndex != -1)
                price = cursor.getFloat(priceColumnIndex)

            val checkedColumnIndex = cursor.getColumnIndex("checked")
            if (dateColumnIndex != -1)
                checked = cursor.getInt(checkedColumnIndex) == 1

            val idParentColumnIndex = cursor.getColumnIndex("idParent")
            if (idParentColumnIndex != -1)
                idParent = cursor.getLong(cursor.getColumnIndexOrThrow("idParent"))
            val idUnitColumnIndex = cursor.getColumnIndex("idUnit")
            if (idUnitColumnIndex != -1)
                idUnit = cursor.getLong(cursor.getColumnIndexOrThrow("idUnit"))
            val idMomentColumnIndex = cursor.getColumnIndex("idMoment")
            if (idMomentColumnIndex != -1)
                idMoment = cursor.getLong(cursor.getColumnIndexOrThrow("idMoment"))
            val idPlaceColumnIndex = cursor.getColumnIndex("idPlace")
            if (idPlaceColumnIndex != -1)
                idPlace = cursor.getLong(cursor.getColumnIndexOrThrow("idPlace"))



        }else{
            Log.e("CURSOR", "The cursor is null.")
        }
    }

    var id: Long = -1
        private set
    var idItem: Long = -1
        private set

    var name: String = ""
        private set

    var amount: Int = 0
        private set
    var amountInventory : Int = -1
        private set

    var date: String = ""
        private set

    var price: Float = -1.0f
        private set

    var checked : Boolean = false
        private set

    var idParent: Long = -1
        private set
    var idUnit: Long = -1
        private set
    var idMoment: Long = -1
        private set
    var idPlace: Long = -1
        private set
    

    fun update(
        id: Long? = null,
        idItem: Long? = null,
        name: String? = null,
        amount: Int? = null,
        amountInventory: Int?=null,
        date: String? = null,
        price: Float? = null,
        checked: Boolean? = null,
        idParent: Long? = null,
        idUnit: Long? = null,
        idMoment: Long? = null,
        idPlace: Long? = null
    ){
        if(id != null)
            this.id = id
        if(idItem != null)
            this.idItem = idItem

        if(name != null)
            this.name = name
        
        if(amount != null)
            this.amount = amount
        if(amountInventory != null)
            this.amountInventory = amountInventory
        
        if(date != null)
            this.date = date
        
        if(price != null)
            this.price = price
        
        if(checked != null)
            this.checked = checked

        if(idParent != null)
            this.idParent = idParent
        if(idUnit != null)
            this.idUnit = idUnit
        if(idMoment != null)
            this.idMoment = idMoment
        if(idPlace != null)
            this.idPlace = idPlace

    }

    fun reset(){
        id  = -1

        name = ""

        amount  = 0
        amountInventory   = -1

        date = ""

        price = -1.0f

        checked = false

        idParent  = -1
        idMoment  = -1
        idUnit  = -1
        idPlace = -1

    }

    override fun toString(): String {
        return """
            {
                "id":$id,
                "name": "$name",
                "amount": $amount,
                "amountInventory": $amountInventory,
                "date": "$date",
                "price": $price,
                "checked": $checked,
                "idParent": $idParent,
                "idItem": $idItem,
                "idUnit": $idUnit,
                "idMoment": $idMoment,
                "idPlace": $idPlace
            }
        """.trimIndent()
    }






}