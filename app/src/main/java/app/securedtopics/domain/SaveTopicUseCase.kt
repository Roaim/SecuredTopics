package app.securedtopics.domain

import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.key.RandomSecretKeyProvider
import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Topic
import app.securedtopics.di.SymmetricMaster
import app.securedtopics.utils.base64
import app.securedtopics.utils.base64decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class SaveTopicUseCase @Inject constructor(
    private val topicRepo: TopicRepo,
    @SymmetricMaster private val cryptography: Cryptography
) {
    suspend operator fun invoke(topic: Topic): Topic? = supervisorScope {
        val encryptedKey = async(Dispatchers.Default) {
            val plainKey = if (topic.key.isBlank())
                RandomSecretKeyProvider().secretKey.encoded
            else topic.key.base64decode
            cryptography.encrypt(plainKey)?.base64
        }.await() ?: return@supervisorScope null
        val topicToSave = topic.copy(key = encryptedKey)
        topicRepo.saveTopic(topicToSave)
        topicToSave
    }
}