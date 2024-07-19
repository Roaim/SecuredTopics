package app.securedtopics.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class LocalMessage(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "topicId")
    val topicId: String,
    @ColumnInfo(name = "sender")
    val sender: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
