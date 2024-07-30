package app.securedtopics.data

import app.securedtopics.data.local.dao.MessageDao
import app.securedtopics.data.mapper.asExternal
import app.securedtopics.data.mapper.asLocal
import app.securedtopics.data.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepo @Inject constructor(
    private val messageDao: MessageDao
) {

    val messages: Flow<List<Message>>
        get() = messageDao.getAll().map { list -> list.map { it.asExternal } }

    suspend fun messagesByTopic(topicId: String): List<Message> =
        messageDao.getAllByTopic(topicId).map { list -> list.asExternal }

    suspend fun saveMessage(message: Message) = messageDao.save(message.asLocal)

    suspend fun updateMessage(message: Message) = messageDao.update(message.asLocal)

}