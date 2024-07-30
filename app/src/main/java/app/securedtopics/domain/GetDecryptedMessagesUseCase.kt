package app.securedtopics.domain

import app.securedtopics.data.MessageRepo
import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Message
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class GetDecryptedMessagesUseCase @Inject constructor(
    private val decryptTextUseCase: DecryptTextUseCase,
    private val messageRepo: MessageRepo,
    private val topicRepo: TopicRepo,
) {
    suspend operator fun invoke(topicId: String?): List<Message> =
        supervisorScope scope@{
            val topic = topicRepo.getTopic(topicId ?: return@scope emptyList())
                ?: return@scope emptyList()
            val msgList = messageRepo.messagesByTopic(topicId)
            msgList.map { msg ->
                async {
                    val decryptedContent = decryptTextUseCase(topic.key, msg.content)
                    msg.copy(content = decryptedContent ?: return@async null)
                }
            }.awaitAll().filterNotNull()
        }
}