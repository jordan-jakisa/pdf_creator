package com.keru.pdfcreator

import androidx.lifecycle.ViewModel
import com.keru.pdfcreator.domain.AppDatabase
import com.keru.pdfcreator.domain.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class AppViewModel: ViewModel() {

}

data class AppUiState(
    val pdfUri: String = "",
    val pdfName: String = "",
    val pdfContent: String = ""
)