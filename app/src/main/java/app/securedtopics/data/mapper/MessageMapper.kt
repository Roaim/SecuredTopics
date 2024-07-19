package app.securedtopics.data.mapper

import app.securedtopics.data.local.model.LocalMessage
import app.securedtopics.data.model.Message

val Message.asLocal: LocalMessage
    get() = LocalMessage(
        id = id,
        topicId = topicId,
        sender = sender,
        content = content,
        timestamp = timestamp
    )

val LocalMessage.asExternal: Message
    get() = Message(
        id = id,
        topicId = topicId,
        sender = sender,
        content = content,
        timestamp = timestamp
    )
