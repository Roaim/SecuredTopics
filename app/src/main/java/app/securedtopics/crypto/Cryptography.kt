package app.securedtopics.crypto

import app.securedtopics.crypto.key.KeyPairProvider
import app.securedtopics.crypto.key.SecretKeyProvider
import app.securedtopics.utils.DefaultLogger
import app.securedtopics.utils.Logger
import java.nio.ByteBuffer
import java.security.KeyPair
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

interface Cryptography {
    fun encrypt(data: ByteArray): ByteArray?
    fun decrypt(encryptedData: ByteArray): ByteArray?
}

class SymmetricCryptography(
    private val secretKeyProvider: SecretKeyProvider,
    private val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding"),
) : Cryptography, Logger by DefaultLogger {

    private val secretKey: SecretKey get() = secretKeyProvider.secretKey
    private val keySize: Int get() = secretKeyProvider.keySize

    override fun encrypt(data: ByteArray): ByteArray? = try {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedData = cipher.doFinal(data)
        val iv = cipher.iv
        ByteBuffer.allocate(4 + iv.size + encryptedData.size).apply {
            putInt(iv.size)
            put(iv)
            put(encryptedData)
        }.array()
    } catch (e: Exception) {
        e.logIt()
        null
    }

    override fun decrypt(encryptedData: ByteArray): ByteArray? = try {
        ByteBuffer.wrap(encryptedData).run {
            val iv = ByteArray(int)
            get(iv)
            val data = ByteArray(remaining())
            get(data)
            val keySpec = GCMParameterSpec(keySize, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, keySpec)
            cipher.doFinal(data)
        }
    } catch (e: Exception) {
        e.logIt()
        null
    }

}

class AsymmetricCryptography(
    private val keyPairProvider: KeyPairProvider,
    private val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"),
) : Cryptography, Logger by DefaultLogger {

    private val keyPair: KeyPair get() = keyPairProvider.keyPair

    override fun encrypt(data: ByteArray): ByteArray? = try {
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.public)
        cipher.doFinal(data)
    } catch (e: Exception) {
        e.logIt()
        null
    }

    override fun decrypt(encryptedData: ByteArray): ByteArray? = try {
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        cipher.doFinal(encryptedData)
    } catch (e: Exception) {
        e.logIt()
        null
    }

}
