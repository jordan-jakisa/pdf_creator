package com.keru.pdfcreator.domain.repositories

import com.keru.pdfcreator.data.Document
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {

    suspend fun getAllDocuments(): Flow<List<Document>>

    suspend fun insert(document: Document)
}