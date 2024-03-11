package com.keru.pdfcreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRightAlt
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPdfCard(modifier: Modifier = Modifier, onClick: () -> Unit) {

    ElevatedCard(modifier = modifier, colors = CardDefaults.elevatedCardColors(
        containerColor = Color(0xFFEE6E6F)
    ), onClick = {
        onClick()
    }) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = "",
                    modifier = Modifier.padding(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = "Capture images with Camera and turn them into PDF file!",
                fontSize = 14.sp,
                color = Color.White
            )
            TextButton(onClick = {
                onClick()
            }) {
                Text(
                    text = "CREATE NEW PDF", color = Color.White, maxLines = 1
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowRightAlt,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}