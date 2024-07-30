package app.securedtopics.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val id: String,
    val topicId: String,
    val sender: String,
    val content: String,
    val publishedAt: Long,
    val timestamp: Long,
)

data class ShareMessage(
    val topicId: String,
    val encryptedMessage: String
)

val Message.publishedAtStr get() = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.ENGLISH)
    .format(Date(publishedAt))
