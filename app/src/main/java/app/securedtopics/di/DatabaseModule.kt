package app.securedtopics.di

import android.content.Context
import androidx.room.Room
import app.securedtopics.data.local.AppDatabase
import app.securedtopics.data.local.dao.TopicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).build()

    @Provides
    @Singleton
    fun provideTopicDao(appDatabase: AppDatabase): TopicDao = appDatabase.topicDao()

}