package com.keru.pdfcreator.di

import com.keru.pdfcreator.data.repositories.DocumentRepositoryImpl
import com.keru.pdfcreator.domain.repositories.DocumentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DocumentModule {

    @Binds
    @Singleton
    abstract fun providesDocumentRepository(documentRepositoryImpl: DocumentRepositoryImpl): DocumentRepository
}