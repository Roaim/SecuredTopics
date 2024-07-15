package app.securedtopics.domain

import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.SecretKey
import app.securedtopics.data.model.Topic
import app.securedtopics.di.Asymmetric
import app.securedtopics.utils.ClipboardService
import app.securedtopics.utils.base64
import app.securedtopics.utils.base64decode
import app.securedtopics.utils.fromJson
import java.nio.ByteBuffer
import javax.inject.Inject

class DecryptTopicUseCase @Inject constructor(
    private val clipboardService: ClipboardService,
    @Asymmetric private val asymmetricCrypto: Cryptography,
) {
    operator fun invoke(): Topic? {
        val topicMessage = clipboardService.clipboardText ?: return null
        return ByteBuffer.wrap(topicMessage.base64decode).run {
            val keyEncrypted = ByteArray(int)
            get(keyEncrypted)
            val topicData = ByteArray(remaining())
            get(topicData)
            val key = asymmetricCrypto.decrypt(keyEncrypted) ?: return@run null
            val secretKey = SecretKey(key)
            val symmetricCrypto = SymmetricCryptography(secretKey)
            symmetricCrypto.decrypt(topicData)
                ?.decodeToString()
                ?.fromJson<Topic>()
                ?.copy(key = key.base64)
        }
    }
}