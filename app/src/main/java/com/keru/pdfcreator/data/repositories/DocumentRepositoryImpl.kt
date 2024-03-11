package com.keru.pdfcreator.data.repositories

import com.keru.pdfcreator.data.Document
import com.keru.pdfcreator.data.DocumentDao
import com.keru.pdfcreator.domain.repositories.DocumentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val documentInfoDao: DocumentDao
): DocumentRepository {

    override suspend fun getAllDocuments(): Flow<List<Document>> {
        return documentInfoDao.getAllDocuments()
    }

    override suspend fun insert(document: Document) {
        documentInfoDao.insert(document)
    }
}