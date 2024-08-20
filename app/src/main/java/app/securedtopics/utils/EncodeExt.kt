package app.securedtopics.utils

import com.google.gson.GsonBuilder

val ByteArray.base64: String get() = DefaultBase64Encoder.encode(this).trim()
val String.base64decode: ByteArray get() = DefaultBase64Encoder.decode(trim())

object DefaultBase64Encoder : Base64Encoder {

    private var encoder: Base64Encoder = AndroidBase64Encoder()

    override fun encode(data: ByteArray): String = encoder.encode(data)

    override fun decode(data: String): ByteArray = encoder.decode(data)

    fun changeEncoder(encoder: Base64Encoder) {
        this.encoder = encoder
    }
}

val Any.json: String get() = GsonBuilder().setPrettyPrinting().create().toJson(this)

inline fun <reified T> String.fromJson(): T? = try {
    GsonBuilder().create().fromJson(this, T::class.java)
} catch (e: Exception) {
    DefaultLogger.log(e)
    null
}