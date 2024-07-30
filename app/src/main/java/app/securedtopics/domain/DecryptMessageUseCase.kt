package app.securedtopics.domain

import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.SecretKeyProvider
import app.securedtopics.data.model.Message
import app.securedtopics.di.SymmetricMaster
import app.securedtopics.utils.base64decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DecryptMessageUseCase @Inject constructor(
    @SymmetricMaster private val cryptography: SymmetricCryptography
) {
    suspend operator fun invoke(encryptedKey: String, message: Message): Message? =
        withContext(Dispatchers.Default) {
            val key = cryptography.decrypt(encryptedKey.base64decode) ?: return@withContext null
            val keyProvider = SecretKeyProvider.fromEncoded(key)
            val symmetricCryptography = SymmetricCryptography(keyProvider)
            val contentDecrypted =
                symmetricCryptography.decrypt(message.content.base64decode)?.decodeToString()
                    ?: return@withContext null
            message.copy(content = contentDecrypted)
        }
}
