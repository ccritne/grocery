package com.example.testapp.utilities

import android.database.Cursor
import android.util.Log
import com.example.testapp.DbManager

fun fromGoogleToApp(dbManager: DbManager){
    val string = "SD2024/06/10;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;Pasta125g;Pumpkin200g;Banana;SM3;SM4;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/11;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;SM2;Pasta50g;Peas80g;JobLunch;Apple;SM3;SM4;Bread100g;Chickpeas125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/12;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;BroadBeans100g;Spinach400g;Bread150g;Apple;SM3;SM4;PizzaTeglia300g;SD2024/06/13;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;SM2;Pasta50g;Beans125g;JobLunch;Banana;SM3;SM4;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/14;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;Pasta80g;Soia130g;Tomatoes160g;Zucchini100g;Carrots100g;Banana;SM3;SM4;Flatbreads250g;PestoSauce95g;Mozzarella125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/15;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;SM2;Pasta125g;Eggs150g;Parmisan25g;Apple;SM3;SM4;Pizza250g;Mozzarella125g;SD2024/06/16;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;Pasta125g;TomatoSauce80g;JobLunch;Banana;SM3;SM4;Bread250g;Chickpeas125g;Zucchini100g;Iceberg/Batavia50g;Tomatoes100g"
    val filterByDay = string.split("SD").toMutableList()

    val foods: MutableList<Food> = mutableListOf()

    filterByDay.forEach{ listDay->
        if (listDay.isNotEmpty()){
            val date = listDay.subSequence(0 .. 9).toString()
            val moments = listDay.subSequence(11, listDay.length).split("SM")

            moments.forEach{ moment ->
                if(moment.isNotEmpty()){
                    val ingredients = moment.split(";")
                    val momentSelector = ingredients[0].toInt()
                    val ingredientsPop= ingredients.subList(1, ingredients.size-1)

                    ingredientsPop.forEach{
                            ingredient ->

                        if (ingredient.isNotEmpty()) {
                            var changeIngredient = ingredient

                            var unit = Units.Pieces
                            if(ingredient.last() == 'g' && ingredient[ingredient.length-2].isDigit()) {
                                unit = Units.Grams
                                changeIngredient =
                                    ingredient.subSequence(0, ingredient.length - 1).toString()
                            }

                            val name = changeIngredient.filter { !it.isDigit() }
                            val amount = changeIngredient.filter { it.isDigit() }

                            val amountInt = if(amount.isNotEmpty()) amount.toInt() else 1

                            val food = Food()

                            food.setUnit(unit)
                            food.update(
                                newDate = date,
                                newMomentSelector = momentSelector,
                                newName = name,
                                newAmount = amountInt
                            )

                            foods.add(food)
                        }

                    }

                }

            }

        }


    }

    foods.forEach {
        food ->
        val pair = dbManager.itemExists(food.name)
        if(!pair.second)
            dbManager.insertItemInventory(food)
        else
            food.setIdInventory(pair.first)

        dbManager.insertFood(food)
    }
}


enum class Units(val symbol: String){
    Grams("g"),
    Pieces("pz");

    override fun toString(): String {
        return symbol
    }
}
enum class Screen(){
    House,
    Menu,
    ShoppingCart,
    UpdateItem
}

enum class AppActions{
    Change,
    Delete,
    Add
}

class Food() {

    constructor(cursor: Cursor? = null, screen: Screen) : this() {
        if(cursor != null) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))

            name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            unit = Units.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("unit")))
            amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"))


            when (screen) {
                Screen.Menu -> {
                    date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                    momentSelector = cursor.getInt(cursor.getColumnIndexOrThrow("momentSelector"))
                    eaten = cursor.getInt(cursor.getColumnIndexOrThrow("eaten")) == 1
                }
                Screen.ShoppingCart ->{
                    idInventory = cursor.getInt(cursor.getColumnIndexOrThrow("idInventory"))
                    amountInventory = cursor.getInt(cursor.getColumnIndexOrThrow("amountInventory"))
                }
                else -> { }
            }


        }else{
            Log.e("CURSOR", "The cursor is null.")
        }
    }

    var id: Int = 0
        private set
    var idParent: Int = -1
        private set

    var idInventory: Int = -1
        private set
    var name: String = ""
        private set
    var amount: Int = 0
        private set

    var amountInventory : Int = -1
        private set
    var unit: Units = Units.Grams
        private set
    var date: String = ""
        private set
    var momentSelector: Int = 0
        private set
    var price: Float = -1.0f
        private set
    var eaten : Boolean = false
        private set

    var mode: AppActions = AppActions.Add
        private set

    fun setMode(actions: AppActions){
        mode = actions
    }

    fun setUnit(unit: Units){
        this.unit = unit
    }

    fun setIdInventory(id: Int){
        this.idInventory = id
    }

    fun update(
        newName: String? = null,
        newAmount: Int? = null,
        newDate: String? = null,
        newMomentSelector: Int? = null,
        newPrice: Float? = null
    ){
        if(newName != null)
            name = newName
        if(newAmount != null)
            amount = newAmount
        if(newDate != null)
            date = newDate
        if(newMomentSelector != null)
            momentSelector = newMomentSelector
        if(newPrice != null)
            price = newPrice
    }

    fun reset(){
        name = ""
        amount = 0
        date = ""
        momentSelector = 0
        price = -1.0f

        id = 0
        idParent = -1
        eaten = false
        amountInventory = -1
    }






}

data class ShoppingCartItem(
    var id: Int = 0,
    var name: String = "",
    var total: Int = 0,
    var checkedTotal: Int = 0,
)