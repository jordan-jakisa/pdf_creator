package com.keru.pdfcreator.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.keru.pdfcreator.R
import com.keru.pdfcreator.data.Document
import com.keru.pdfcreator.ui.components.CompletedDialog
import com.keru.pdfcreator.ui.components.CreateNewPdfCard
import com.keru.pdfcreator.ui.components.DocumentCard
import com.keru.pdfcreator.ui.components.EmptyListComponent
import com.keru.pdfcreator.ui.components.FeatureCard
import com.keru.pdfcreator.ui.theme.PDFCreatorTheme
import com.keru.pdfcreator.utils.formatDate
import com.keru.pdfcreator.utils.openPdfFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: AppViewModel by viewModels()

    private var imageUri by mutableStateOf<Uri?>(null)
    private var pdfUri by mutableStateOf<Uri?>(null)
    private var pageCount by mutableIntStateOf(0)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scannerLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    handleResult(result)
                }
            }

        setContent {
            PDFCreatorTheme {
                //document scanner options
                val options = GmsDocumentScannerOptions.Builder()
                    .setGalleryImportAllowed(true)
                    .setPageLimit(100)
                    .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG)
                    .setScannerMode(SCANNER_MODE_FULL)
                    .build()

                val scanner = GmsDocumentScanning.getClient(options)
                val uiState = vm.uiState
                val scrollState = rememberScrollState()
                val context = LocalContext.current

                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = "Hello,",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Create PDF files instantly!",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                CreateNewPdfCard(modifier = Modifier.weight(1f)) {
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
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    FeatureCard(
                                        icon = R.drawable.image, title = "Img to PDF"
                                    ) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Img to PDF is not available yet",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    FeatureCard(icon = R.drawable.compress, title = "Compress") {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Compress feature is not available yet",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = "Recent Files", fontWeight = FontWeight.Bold
                            )

                            AnimatedVisibility(visible = uiState.documents.isNotEmpty(),
                                enter = slideInVertically { it },
                                exit = slideOutVertically { it }) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.padding(vertical = 16.dp)
                                ) {
                                    uiState.documents.forEach { document ->
                                        DocumentCard(document = document)
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = uiState.documents.isEmpty(),
                                enter = slideInHorizontally { it },
                                exit = slideOutVertically { it }
                            ) {
                                EmptyListComponent()
                            }
                        }
                    }


                    pdfUri?.let {
                        if (uiState.documents.isNotEmpty()) {
                            AnimatedVisibility(
                                visible = it.path == uiState.documents.first().fileUri.toUri().path,
                                enter = slideInVertically { it },
                                exit = slideOutVertically { it }
                            ) {
                                CompletedDialog(
                                    document = uiState.documents.first(),
                                    onShare = {
                                        context.openPdfFile(it)
                                    },
                                    onDismiss = {
                                        pdfUri = null
                                        imageUri = null
                                        pageCount = 0
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }
    }

    private fun handleResult(result: ActivityResult) {
        val currentTime = System.currentTimeMillis()
        val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
        scanResult?.pdf?.let { pdf ->
            pdfUri = pdf.uri
            pageCount = pdf.pageCount

            val document = Document(
                name = "Document ${
                    currentTime.formatDate().replace(" ", "_").replace(",", "_")
                }_${currentTime.toString().takeLast(4)}",
                fileUri = pdfUri.toString(),
                pageCount = pageCount,
                createdAt = currentTime
            )
            vm.insertDocument(document)
        }
    }
}
