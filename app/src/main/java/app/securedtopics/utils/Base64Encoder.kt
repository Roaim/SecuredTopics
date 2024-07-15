package app.securedtopics.utils

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import kotlin.io.encoding.ExperimentalEncodingApi

interface Base64Encoder {
    fun encode(data: ByteArray): String
    fun decode(data: String): ByteArray
}

class AndroidBase64Encoder(private val flag: Int = Base64.DEFAULT) : Base64Encoder {
    override fun encode(data: ByteArray): String = Base64.encodeToString(data, flag)

    override fun decode(data: String): ByteArray = Base64.decode(data, flag)
}

@RequiresApi(Build.VERSION_CODES.O)
class JavaBase64Encoder : Base64Encoder {
    private val encoder get() = java.util.Base64.getEncoder()
    private val decoder get() = java.util.Base64.getDecoder()

    override fun encode(data: ByteArray): String = encoder.encodeToString(data)

    override fun decode(data: String): ByteArray = decoder.decode(data)
}

@OptIn(ExperimentalEncodingApi::class)
class KotlinBase64Encoder : Base64Encoder {
    override fun encode(data: ByteArray): String = kotlin.io.encoding.Base64.encode(data)

    override fun decode(data: String): ByteArray = kotlin.io.encoding.Base64.decode(data)
}
