package app.securedtopics.domain

import app.securedtopics.data.MessageRepo
import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Message
import app.securedtopics.data.model.ShareMessage
import app.securedtopics.utils.ClipboardService
import app.securedtopics.utils.fromJson
import javax.inject.Inject

class ImportMessageUseCase @Inject constructor(
    private val clipboardService: ClipboardService,
    private val decryptTextUseCase: DecryptTextUseCase,
    private val topicRepo: TopicRepo,
    private val messageRepo: MessageRepo,
) {
    suspend operator fun invoke(): String? {
        val clipboardText = clipboardService.clipboardText ?: return null
        val data = clipboardText.fromJson<ShareMessage>() ?: return null
        val topic = topicRepo.getTopic(data.topicId) ?: return null
        val decryptedMessage =
            decryptTextUseCase(topic.key, data.encryptedMessage)?.fromJson<Message>() ?: return null
        messageRepo.saveMessage(decryptedMessage)
        return "Message from ${decryptedMessage.sender} is imported to Topic, ${topic.id}"
    }
}
