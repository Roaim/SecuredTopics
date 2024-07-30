package app.securedtopics.domain

import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Message
import app.securedtopics.data.model.ShareMessage
import app.securedtopics.utils.ClipboardService
import app.securedtopics.utils.json
import javax.inject.Inject

class ExportMessageUseCase @Inject constructor(
    private val encryptTextUseCase: EncryptTextUseCase,
    private val clipboardService: ClipboardService,
    private val topicRepo: TopicRepo,
) {
    suspend operator fun invoke(message: Message) {
        val topic = topicRepo.getTopic(message.topicId) ?: return
        val encryptedContent = encryptTextUseCase(topic.key, message.content) ?: return
        val msgToExport = message.copy(content = encryptedContent)
        val encryptedMessage = encryptTextUseCase(topic.key, msgToExport.json) ?: return
        val data = ShareMessage(topic.id, encryptedMessage).json
        clipboardService.copyToClipboard(data, "message")
    }
}
