package app.securedtopics.utils

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class Base64EncoderTest {

    @Test
    fun javaEncoder() {
        DefaultBase64Encoder.changeEncoder(JavaBase64Encoder())
        val expected = "Hello Java"
        val encoded = expected.toByteArray().base64
        println("Encoded: $encoded")
        val decoded = encoded.base64decode.decodeToString()
        println("Decoded: $decoded")
        assertEquals(expected, decoded)
    }

    @Test
    fun kotlinEncoder() {
        DefaultBase64Encoder.changeEncoder(KotlinBase64Encoder())
        val expected = "Hello Kotlin"
        val encoded = expected.toByteArray().base64
        println("Encoded: $encoded")
        val decoded = encoded.base64decode.decodeToString()
        println("Decoded: $decoded")
        assertEquals(expected, decoded)
    }
}