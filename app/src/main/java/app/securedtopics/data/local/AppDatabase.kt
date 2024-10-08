package app.securedtopics.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import app.securedtopics.data.local.dao.MessageDao
import app.securedtopics.data.local.dao.TopicDao
import app.securedtopics.data.local.model.LocalMessage
import app.securedtopics.data.local.model.LocalTopic

@Database(entities = [LocalTopic::class, LocalMessage::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun messageDao(): MessageDao
}
