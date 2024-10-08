package app.securedtopics.domain

import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.SecretKeyProvider
import app.securedtopics.data.model.Topic
import app.securedtopics.di.AsymmetricStore
import app.securedtopics.utils.ClipboardService
import app.securedtopics.utils.base64
import app.securedtopics.utils.base64decode
import app.securedtopics.utils.fromJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import javax.inject.Inject

class DecryptClipboardTopicUseCase @Inject constructor(
    private val clipboardService: ClipboardService,
    @AsymmetricStore private val asymmetricCrypto: Cryptography,
) {
    suspend operator fun invoke(): Topic? = withContext(Dispatchers.Default) {
        val topicMessage = clipboardService.clipboardText ?: return@withContext null
        ByteBuffer.wrap(topicMessage.base64decode).run {
            val keyEncrypted = ByteArray(int)
            get(keyEncrypted)
            val topicData = ByteArray(remaining())
            get(topicData)
            val key = asymmetricCrypto.decrypt(keyEncrypted) ?: return@run null
            val secretKeyProvider = SecretKeyProvider.fromEncoded(key)
            val symmetricCrypto = SymmetricCryptography(secretKeyProvider)
            symmetricCrypto.decrypt(topicData)
                ?.decodeToString()
                ?.fromJson<Topic>()
                ?.copy(key = key.base64)
        }
    }
}
