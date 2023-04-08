package com.farmer.data

import android.content.Context
import androidx.room.Room
import com.farmer.data.repository.OliveRepository
import com.farmer.data.repository.OliveRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModule {

    @Binds
    abstract fun bindOliveRepository(repoImpl: OliveRepositoryImpl): OliveRepository

    companion object {
        @Provides
        @Singleton
        fun provideOliveDatabase(
            @ApplicationContext context: Context
        ): OliveDatabase {
            return Room.databaseBuilder(
                context,
                OliveDatabase::class.java,
                "olive.db"
            ).build()
        }

        @Provides
        fun provideOliveDAO(oliveDatabase: OliveDatabase): OliveDao {
            return oliveDatabase.oliveDao()
        }
    }
}
