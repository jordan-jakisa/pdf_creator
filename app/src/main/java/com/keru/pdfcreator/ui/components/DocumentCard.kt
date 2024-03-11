package com.keru.pdfcreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.keru.pdfcreator.R
import com.keru.pdfcreator.data.Document
import com.keru.pdfcreator.utils.getTimeAgo
import com.keru.pdfcreator.utils.openPdfFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentCard(document: Document) {
    val context = LocalContext.current
    ElevatedCard(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFf2f6f9)
        ),
        onClick = {
            context.openPdfFile(document.fileUri.toUri())
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.pdf_file),
                contentDescription = "",
                modifier = Modifier.size(54.dp),
                tint = Color(0xFFee6e6f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = document.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = document.pageCount.toString() + " pages, " + document.createdAt.getTimeAgo(),
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .5f
                    ),
                    fontSize = 12.sp
                )
            }
        }
    }

}