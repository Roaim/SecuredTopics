package app.securedtopics.domain

import app.securedtopics.crypto.AsymmetricCryptography
import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.CryptoKey
import app.securedtopics.crypto.key.PublicKey
import app.securedtopics.crypto.key.SecretKey
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

class CopyTopicToClipboardUseCase @Inject constructor(
    private val clipboardService: ClipboardService,
    @SymmetricMaster private val masterCryptography: Cryptography
) {
    suspend operator fun invoke(topic: Topic) = withContext(Dispatchers.Default) {
        val publicKeyEncoded = clipboardService.clipboardText ?: return@withContext
        val publicKey: CryptoKey = PublicKey(publicKeyEncoded)
        val asymmetricCrypto = AsymmetricCryptography(publicKey)
        val key = masterCryptography.decrypt(topic.key.base64decode) ?: return@withContext
        val secretKey = SecretKey(key)
        val symmetricCrypto = SymmetricCryptography(secretKey)
        val message = symmetricCrypto.encrypt(topic.json.toByteArray()) ?: return@withContext
        val encryptedKey = asymmetricCrypto.encrypt(key) ?: return@withContext
        val data = ByteBuffer.allocate(4 + encryptedKey.size + message.size).apply {
            putInt(encryptedKey.size)
            put(encryptedKey)
            put(message)
        }.array().base64
        clipboardService.copyToClipboard(data, "key-exchange")
    }
}