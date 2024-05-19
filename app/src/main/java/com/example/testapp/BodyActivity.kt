package com.example.testapp

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.testapp.body.House
import com.example.testapp.body.menu.Menu
import com.example.testapp.body.ShoppingCart

// Global variable
val screen = mutableStateOf("CALENDAR")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Body(context: Context){
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        when (screen.value) {
            "HOUSE" -> House()
            "CALENDAR" -> Menu(context)
            "SHOPPING CART" -> ShoppingCart()
            else -> Text(text = "404")
        }
    }
}