package app.securedtopics.domain

import app.securedtopics.data.AppUserRepo
import app.securedtopics.data.model.Message
import java.util.UUID
import javax.inject.Inject

class CreateMessageUseCase @Inject constructor(
    private val userRepo: AppUserRepo
) {
    suspend operator fun invoke(topicId: String, message: String): Message {
        val userName = userRepo.appUserName()
        return Message(
            id = UUID.randomUUID().toString(),
            topicId = topicId,
            sender = userName,
            content = message,
            publishedAt = System.currentTimeMillis(),
            timestamp = System.currentTimeMillis()
        )
    }
}
