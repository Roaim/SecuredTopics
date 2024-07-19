package app.securedtopics.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class LocalTopic(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "key")
    val key: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
)
