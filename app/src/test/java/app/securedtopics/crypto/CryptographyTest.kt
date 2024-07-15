package app.securedtopics.crypto

import app.securedtopics.crypto.key.TemporaryKeyPairProvider
import app.securedtopics.crypto.key.TemporarySecretKeyProvider
import app.securedtopics.utils.DefaultBase64Encoder
import app.securedtopics.utils.JavaBase64Encoder
import app.securedtopics.utils.base64
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CryptographyTest {

    @Before
    fun setUp() {
        DefaultBase64Encoder.changeEncoder(JavaBase64Encoder())
    }

    @Test
    fun symmetric_encrypt_decrypt() {
        val encryption = SymmetricCryptography(TemporarySecretKeyProvider())
        val text = "Hello Secret Text"
        println("Base64: ${text.toByteArray().base64}")
        val encrypted = encryption.encrypt(text.toByteArray())
        println("Encrypted: ${encrypted?.base64}")
        assertNotNull(encrypted)
        val decrypted = encryption.decrypt(encrypted!!)?.decodeToString()
        println("Decrypted: $decrypted")
        assertEquals(text, decrypted)
    }

    @Test
    fun asymmetric_encrypt_decrypt() {
        val encryption = AsymmetricCryptography(TemporaryKeyPairProvider())
        val text = "Hello Secret Text"
        println("Base64: ${text.toByteArray().base64}")
        val encrypted = encryption.encrypt(text.toByteArray())
        println("Encrypted: ${encrypted?.base64}")
        assertNotNull(encrypted)
        val decrypted = encryption.decrypt(encrypted!!)?.decodeToString()
        println("Decrypted: $decrypted")
        assertEquals(text, decrypted)
    }

}