package com.keru.pdfcreator.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.openPdfFile(pdfUri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(pdfUri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Handle the case where a PDF viewer app is not installed on the device
        Toast.makeText(this, "No PDF viewer found", Toast.LENGTH_SHORT).show()
    }
}