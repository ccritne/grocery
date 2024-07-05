package com.example.grocery.utilities

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * Load and display image from URI into ImageView.
 * @param context Context of the calling activity or application.
 * @param uri The URI of the image file.
 * @param imageView The ImageView where the image will be displayed.
 */
fun loadImageFromUri(activity: Activity, context: Context, uri: Uri) : Bitmap? {
    // Check for permission before proceeding
    // Permission is not granted, request it
    ActivityCompat.requestPermissions(activity,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        100)


    var inputStream: InputStream? = null
    try {
        inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        return bitmap
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        // Handle error
    } finally {
        inputStream?.close()
    }


    return null
}