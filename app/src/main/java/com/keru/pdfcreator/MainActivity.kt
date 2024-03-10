package com.keru.pdfcreator

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.keru.pdfcreator.ui.theme.PDFCreatorTheme
import com.keru.pdfcreator.utils.openPdfFile

class MainActivity : ComponentActivity() {
    private var imageUri: Uri? = null
    private var pdfUri: Uri? = null
    private var pageCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Move the registration outside the setContent block
        val scannerLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val scanResult =
                        GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                    scanResult?.pages?.let { pages ->
                        for (page in pages) {
                            imageUri = pages[0].imageUri
                        }
                    }

                    scanResult?.pdf?.let { pdf ->
                        pdfUri = pdf.uri
                        pageCount = pdf.pageCount
                    }
                }
            }


        setContent {
            PDFCreatorTheme {

                //document scanner options
                val options =
                    GmsDocumentScannerOptions.Builder().setGalleryImportAllowed(true)
                        .setPageLimit(100)
                        .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG).setScannerMode(
                            SCANNER_MODE_FULL
                        ).build()

                val scanner = GmsDocumentScanning.getClient(options)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = pageCount == 0,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it }) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Button(onClick = {
                                    scanner.getStartScanIntent(this@MainActivity)
                                        .addOnSuccessListener { intentSender ->
                                            scannerLauncher.launch(
                                                IntentSenderRequest.Builder(intentSender).build()
                                            )
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "${it.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }) {
                                    Text(text = "Scan")
                                }
                            }

                        }

                        AnimatedVisibility(visible = pageCount != 0,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it }) {
                            Column {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(4 / 3f)
                                )

                                Text(text = "Number of pages: $pageCount")
                                Button(onClick = {
                                    pdfUri?.let {
                                        this@MainActivity.openPdfFile(it)
                                    }
                                }) {
                                    Text(text = "Open the PDF file")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}