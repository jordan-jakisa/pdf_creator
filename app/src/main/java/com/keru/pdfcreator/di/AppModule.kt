package com.keru.pdfcreator.di

import android.content.Context
import androidx.room.Room
import com.keru.pdfcreator.data.AppDatabase
import com.keru.pdfcreator.data.DocumentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    @Provides
    @Singleton
    fun providesDocumentDao(appDatabase: AppDatabase): DocumentDao {
        return appDatabase.documentDao()
    }

}