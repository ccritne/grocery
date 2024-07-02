package com.example.grocery.utilities

import com.example.grocery.database.DbManager
import com.example.grocery.database.insertItemIntoList
import com.example.grocery.database.insertMoment
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.itemExists
import com.example.grocery.items.Item
import java.text.SimpleDateFormat
import java.util.Locale

fun <T, R> fromPairToMapEntry(pair : Pair<T, R>) : Map.Entry<T, R> {
    return object : Map.Entry<T, R> {
        override val key: T
            get() = pair.first
        override val value: R
            get() = pair.second
    }
}

fun getFormatterDateSql() : SimpleDateFormat {
    return SimpleDateFormat("y/MM/dd", Locale.getDefault())
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

                            val copy = item.copy(
                                name = name,
                                amount = amountInt,
                                date = date,
                                idMoment = momentChoice,
                                idUnit = unit,
                                idPlace = idSideboard
                            )

                            items.add(copy)
                        }

                    }

                }

            }

        }


    }

    val clothItem = Item().copy(
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


        val localCopy = item.copy(idItem = idItem, amountInventory = 0)

        dbManager.insertPlanItem(item)
    }
}


