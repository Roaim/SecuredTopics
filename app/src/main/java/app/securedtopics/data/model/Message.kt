package app.securedtopics.data.model

data class Message(
    val id: String,
    val topicId: String,
    val sender: String,
    val content: String,
    val timestamp: Long,
)
