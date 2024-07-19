package app.securedtopics.crypto.key

import app.securedtopics.utils.DefaultBase64Encoder
import app.securedtopics.utils.JavaBase64Encoder
import app.securedtopics.utils.base64
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class SecretKeyProviderTest {

    @Before
    fun setUp() {
        DefaultBase64Encoder.changeEncoder(JavaBase64Encoder())
    }

    @Test
    fun testRandom() {
        val randomProvider1 = RandomSecretKeyProvider()
        val randomProvider2 = RandomSecretKeyProvider()
        assertNotEquals(
            randomProvider1.secretKey.encoded.base64,
            randomProvider2.secretKey.encoded.base64,
        )
    }

    @Test
    fun random_raw_provider() {
        val randomProvider = RandomSecretKeyProvider()
        val rawProvider =
            EncodedSecretKeyProvider(
                randomProvider.secretKey.encoded,
                keySize = randomProvider.keySize
            )
        assertEquals(randomProvider.keySize, randomProvider.keySize)
        assertEquals(randomProvider.secretKey.encoded.base64, rawProvider.secretKey.encoded.base64)
    }

}