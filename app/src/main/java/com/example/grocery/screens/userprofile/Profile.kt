package com.example.grocery.screens.userprofile

import android.R.attr.bitmap
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.utilities.loadImageFromUri
import java.io.File
import java.io.FileOutputStream


@Composable
fun Profile(app: App) {

    val cw = ContextWrapper(app.applicationContext)

    // path to /data/data/yourapp/app_data/imageDir
    val directory = cw.getDir("temps", Context.MODE_PRIVATE)
    val filepath = directory.absolutePath+"/temp.jpg"

    var imageBitmap by remember {
        mutableStateOf(
                if(File(filepath).exists())
                    BitmapFactory.decodeFile(filepath)
                else
                    null
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        // Create imageDir
        val userImage = File(directory,  "temp.jpg")

        val fos: FileOutputStream
        try {
            fos = FileOutputStream(userImage)

            if (uri != null) {
                imageBitmap = loadImageFromUri(app, app.applicationContext, uri)
                imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }

            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageBitmap != null)
            Image(
                modifier = Modifier.size(200.dp)
                    .clip(CircleShape)
                    .clickable {
                    galleryLauncher.launch("image/*")
                },
                contentScale = ContentScale.Crop,
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "user image")
        else
            Button(
                shape = CircleShape,
                modifier = Modifier.size(200.dp),
                onClick = { galleryLauncher.launch("image/*") }
            ) {
                Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = "Photo", modifier = Modifier.fillMaxSize())
            }

        Text(
            text = "Your name",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 35.sp
        )
    }
}