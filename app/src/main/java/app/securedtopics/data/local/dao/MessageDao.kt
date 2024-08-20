package app.securedtopics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.securedtopics.data.local.model.LocalMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(message: LocalMessage)

    @Update
    suspend fun update(message: LocalMessage)

    @Query("SELECT * FROM messages WHERE topicId = :topicId ORDER BY timestamp ASC")
    fun getMessagesByTopic(topicId: String): Flow<List<LocalMessage>>

}
