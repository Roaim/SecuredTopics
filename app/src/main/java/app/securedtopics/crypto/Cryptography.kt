package app.securedtopics.crypto

import app.securedtopics.crypto.key.CryptoKey
import app.securedtopics.utils.DefaultLogger
import app.securedtopics.utils.Logger
import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

interface Cryptography {
    fun encrypt(data: ByteArray): ByteArray?
    fun decrypt(encryptedData: ByteArray): ByteArray?
}

class SymmetricCryptography(
    private val cryptoKey: CryptoKey,
    private val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding"),
) : Cryptography, Logger by DefaultLogger {

    override fun encrypt(data: ByteArray): ByteArray? = try {
        cipher.init(Cipher.ENCRYPT_MODE, cryptoKey.encryptKey)
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

    override fun decrypt(encryptedData: ByteArray): ByteArray? = cryptoKey.decryptKey?.let { key ->
        try {
            ByteBuffer.wrap(encryptedData).run {
                val iv = ByteArray(int)
                get(iv)
                val data = ByteArray(remaining())
                get(data)
                val keySpec = GCMParameterSpec(cryptoKey.size, iv)
                cipher.init(Cipher.DECRYPT_MODE, key, keySpec)
                cipher.doFinal(data)
            }
        } catch (e: Exception) {
            e.logIt()
            null
        }
    }

}

class AsymmetricCryptography(
    private val cryptoKey: CryptoKey,
    private val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"),
) : Cryptography, Logger by DefaultLogger {

    override fun encrypt(data: ByteArray): ByteArray? = try {
        cipher.init(Cipher.ENCRYPT_MODE, cryptoKey.encryptKey)
        cipher.doFinal(data)
    } catch (e: Exception) {
        e.logIt()
        null
    }

    override fun decrypt(encryptedData: ByteArray): ByteArray? = cryptoKey.decryptKey?.let { key ->
        try {
            cipher.init(Cipher.DECRYPT_MODE, key)
            cipher.doFinal(encryptedData)
        } catch (e: Exception) {
            e.logIt()
            null
        }
    }
}
