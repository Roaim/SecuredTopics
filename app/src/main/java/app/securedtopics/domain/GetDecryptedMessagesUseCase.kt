package app.securedtopics.domain

import app.securedtopics.data.MessageRepo
import app.securedtopics.data.model.Message
import app.securedtopics.data.model.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDecryptedMessagesUseCase @Inject constructor(
    private val decryptTextUseCase: DecryptTextUseCase,
    private val messageRepo: MessageRepo,
) {
    private val cache = mutableMapOf<String, String>()

    suspend operator fun invoke(topic: Topic): Flow<List<Message>> =
        messageRepo.messagesByTopic(topic.id).map { list ->
            list.decrypt(topic.key)
        }

    private suspend fun List<Message>.decrypt(key: String) = withContext(Dispatchers.Default) {
        map { msg ->
            async {
                val decryptedContent =
                    cache[msg.id] ?: decryptTextUseCase(key, msg.content) ?: return@async null
                cache[msg.id] = decryptedContent
                msg.copy(content = decryptedContent)
            }
        }.awaitAll().filterNotNull()
    }
}