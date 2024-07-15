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

    @Query("SELECT * FROM topic")
    fun getAll(): Flow<List<LocalTopic>>

    @Query("SELECT * FROM topic WHERE id = :id limit 1")
    fun getById(id: String): Flow<LocalTopic?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(topic: LocalTopic)

    @Delete
    suspend fun delete(topic: LocalTopic)

}
