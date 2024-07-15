package app.securedtopics.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic")
data class LocalTopic(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "key")
    val key: String,
    @PrimaryKey
    val id: String,
)
