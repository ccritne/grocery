package com.example.grocery.body.menu

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Date(
    date: MutableState<LocalDate>,
    enableLeft: Boolean,
    enableRight: Boolean,
    fontSizeText: Int,
    modifier: Modifier = Modifier,
    modifierIcons: Modifier = Modifier,
){


    val formatterDesign = DateTimeFormatter.ofPattern("dd/MM")
    val formattedDateDesign = date.value.format(formatterDesign)


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = enableLeft,
            onClick = {
                date.value = date.value.minusDays(1)
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
                date.value = date.value.plusDays(1)
            }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "forwardDate",
                modifier = modifierIcons
            )
        }
    }
}
