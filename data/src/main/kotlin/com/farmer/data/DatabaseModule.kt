package com.farmer.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farmer.data.repository.OliveRepository
import com.farmer.data.repository.OliveRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

//기본으로 제공되는 카테고리 리스트
val BASIC_CATEGORY = arrayListOf(
    Category("급여"),
    Category("용돈"),
    Category("이체"),
    Category("카드대금"),
    Category("고정지출"),
    Category("저축"),
    Category("식비"),
    Category("여가비"),
    Category("생활비"),
    Category("교통비"),
    Category("병원/의료비"),
    Category("문화/관광비")
)


@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModule {

    @Binds
    abstract fun bindOliveRepository(repoImpl: OliveRepositoryImpl): OliveRepository

    companion object {
        private var instance: OliveDatabase? = null
        @Provides
        @Singleton
        fun provideOliveDatabase(
            @ApplicationContext context: Context
        ): OliveDatabase {
            return instance ?: synchronized(this) {Room.databaseBuilder(
                context,
                OliveDatabase::class.java,
                "olive.db"
            ).addCallback(
                object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            provideOliveDatabase(context).oliveDao().insertBasicList(BASIC_CATEGORY)
                        }
                    }
                }
            )//.addMigrations(MIGRATION_1_2)
            .build()
            }.also {
                instance = it
            }
        }

       /* private val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE CATEGORY ('categoryname' TEXT NOT NULL, 'id' INTEGER NOT NULL, PRIMARY KEY('id'))")
            }
        }*/


        @Provides
        fun provideOliveDAO(oliveDatabase: OliveDatabase): OliveDao {
            return oliveDatabase.oliveDao()
        }

        /*@Provides
        fun provideCategoryDAO(oliveDatabase: OliveDatabase): CategoryDao{
            return oliveDatabase.categoryDao()
        }*/
    }
}
