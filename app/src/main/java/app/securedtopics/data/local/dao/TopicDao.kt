package app.securedtopics.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.securedtopics.data.local.model.LocalTopic
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics")
    fun getAll(): Flow<List<LocalTopic>>

    @Query("SELECT * FROM topics WHERE id = :id limit 1")
    suspend fun getById(id: String): LocalTopic?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(topic: LocalTopic)

    @Delete
    suspend fun delete(topic: LocalTopic)

}
