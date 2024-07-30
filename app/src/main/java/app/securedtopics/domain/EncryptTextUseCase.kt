package app.securedtopics.domain

import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.SecretKeyProvider
import app.securedtopics.di.SymmetricMaster
import app.securedtopics.utils.base64
import app.securedtopics.utils.base64decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EncryptTextUseCase @Inject constructor(
    @SymmetricMaster private val cryptography: Cryptography
) {
    suspend operator fun invoke(encryptedKey: String, text: String): String? =
        withContext(Dispatchers.Default) {
            val key = cryptography.decrypt(encryptedKey.base64decode) ?: return@withContext null
            val keyProvider = SecretKeyProvider.fromEncoded(key)
            val symmetricCryptography = SymmetricCryptography(keyProvider)
            symmetricCryptography.encrypt(text.toByteArray())?.base64
        }
}