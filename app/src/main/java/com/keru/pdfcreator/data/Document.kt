package com.keru.pdfcreator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Document(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val fileUri: String,
    val createdAt: Long,
    val pageCount: Int,
)