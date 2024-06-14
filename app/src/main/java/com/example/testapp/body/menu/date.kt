package com.example.testapp.body.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.MainActivity
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Date(app: MainActivity, enableLeft: Boolean, enableRight: Boolean){


    val formatterDesign = DateTimeFormatter.ofPattern("dd MMM y")
    var date = app.date
    val formattedDateDesign = date.format(formatterDesign)


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = enableLeft,
            onClick = {
                date = date.minusDays(1)
                app.date  = date
            }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "backDate",
                modifier = Modifier.size(25.dp)
            )
        }
        TextButton(
            onClick = { },
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
        ) {
            Text(
                text = formattedDateDesign,
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
            )
        }
        IconButton(
            enabled = enableRight,
            onClick = {
                date = date.plusDays(1)
                app.date = date
            }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "forwardDate",
                modifier = Modifier.size(25.dp)
            )
        }
    }
}
