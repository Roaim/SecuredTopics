package app.securedtopics.domain

import app.securedtopics.crypto.AsymmetricCryptography
import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.KeyPairProvider
import app.securedtopics.crypto.key.SecretKeyProvider
import app.securedtopics.data.model.Topic
import app.securedtopics.di.SymmetricMaster
import app.securedtopics.utils.ClipboardService
import app.securedtopics.utils.base64
import app.securedtopics.utils.base64decode
import app.securedtopics.utils.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import javax.inject.Inject

class ExportTopicUseCase @Inject constructor(
    private val clipboardService: ClipboardService,
    @SymmetricMaster private val masterCryptography: Cryptography
) {
    suspend operator fun invoke(topic: Topic): Boolean = withContext(Dispatchers.Default) {
        val publicKeyEncoded = clipboardService.clipboardText ?: return@withContext false
        val keyPairProvider = KeyPairProvider.fromEncoded(publicKeyEncoded)
        val asymmetricCrypto = AsymmetricCryptography(keyPairProvider)
        val key = masterCryptography.decrypt(topic.key.base64decode) ?: return@withContext false
        val secretKeyProvider = SecretKeyProvider.fromEncoded(key)
        val symmetricCrypto = SymmetricCryptography(secretKeyProvider)
        val message = symmetricCrypto.encrypt(topic.json.toByteArray()) ?: return@withContext false
        val encryptedKey = asymmetricCrypto.encrypt(key) ?: return@withContext false
        val data = ByteBuffer.allocate(4 + encryptedKey.size + message.size).apply {
            putInt(encryptedKey.size)
            put(encryptedKey)
            put(message)
        }.array().base64
        clipboardService.copyToClipboard(data, "key-exchange")
        true
    }
}