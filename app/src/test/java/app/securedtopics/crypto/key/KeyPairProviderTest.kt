package app.securedtopics.crypto.key

import app.securedtopics.utils.DefaultBase64Encoder
import app.securedtopics.utils.JavaBase64Encoder
import app.securedtopics.utils.base64
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class KeyPairProviderTest {

    @Before
    fun setUp() {
        DefaultBase64Encoder.changeEncoder(JavaBase64Encoder())
    }

    @Test
    fun testRandom() {
        val randomProvider1 = RandomKeyPairProvider()
        val randomProvider2 = RandomKeyPairProvider()
        assertNotEquals(
            randomProvider1.keyPair.private.encoded.base64,
            randomProvider2.keyPair.private.encoded.base64,
        )
        assertNotEquals(
            randomProvider1.keyPair.public.encoded.base64,
            randomProvider2.keyPair.public.encoded.base64,
        )
    }

    @Test
    fun random_raw_provider() {
        val randomProvider = RandomKeyPairProvider()
        val rawProvider = EncodedKeyPairProvider(
            encodedPrivateKey = randomProvider.keyPair.private.encoded.base64,
            encodedPublicKey = randomProvider.keyPair.public.encoded.base64
        )
        assertEquals(
            randomProvider.keyPair.private.encoded.base64,
            rawProvider.keyPair.private.encoded.base64
        )
        assertEquals(
            randomProvider.keyPair.public.encoded.base64,
            rawProvider.keyPair.public.encoded.base64
        )
    }

}