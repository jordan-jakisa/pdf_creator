package com.keru.pdfcreator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keru.pdfcreator.data.Document
import com.keru.pdfcreator.domain.repositories.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
) : ViewModel() {

    var uiState by mutableStateOf(AppUiState())
        private set

    init {
        getAllDocuments()
    }

    private fun getAllDocuments() {
        viewModelScope.launch {
            documentRepository.getAllDocuments().collect { documents ->
                uiState = uiState.copy(documents = documents.sortedByDescending { it.createdAt })
            }
        }
    }

    fun insertDocument(document: Document) {
        viewModelScope.launch {
            documentRepository.insert(document)
        }
    }

}

data class AppUiState(
    val documents: List<Document> = emptyList(),
)