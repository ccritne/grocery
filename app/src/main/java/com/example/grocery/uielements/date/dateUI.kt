package com.example.grocery.uielements.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun Date(
    date: Date,
    enableLeft: Boolean,
    enableRight: Boolean,
    fontSizeText: Int,
    modifier: Modifier = Modifier,
    modifierIcons: Modifier = Modifier,
    onChange: (Date) -> Unit
){

    val formatterDesign = SimpleDateFormat("E dd/MM", Locale.getDefault())
    val formattedDateDesign = formatterDesign.format(date)


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = enableLeft,
            onClick = {
                onChange(getUpdateDate(date, -1))
            }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "backDate",
                modifier = modifierIcons
            )
        }
        TextButton(
            onClick = { },
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
        ) {
            Text(
                text = formattedDateDesign,
                fontSize = fontSizeText.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
            )
        }
        IconButton(
            enabled = enableRight,
            onClick = {
                onChange(getUpdateDate(date, 1))
            }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "forwardDate",
                modifier = modifierIcons
            )
        }
    }
}