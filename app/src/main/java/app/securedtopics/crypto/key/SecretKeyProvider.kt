package app.securedtopics.crypto.key

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

interface SecretKeyProvider {
    val keySize: Int
    val secretKey: SecretKey

    companion object {
        fun fromRawKey(
            rawKey: ByteArray, algorithm: String = KEY_ALGORITHM_SYMMETRIC
        ): SecretKeyProvider = RawSecretKeyProvider(rawKey, algorithm)
    }
}

class RawSecretKeyProvider(
    private val rawKey: ByteArray,
    private val algorithm: String = KEY_ALGORITHM_SYMMETRIC
) : SecretKeyProvider {
    override val keySize: Int = KEY_SIZE_SYMMETRIC
    override val secretKey: SecretKey by lazy {
        SecretKeySpec(rawKey, algorithm)
    }
}

class TemporarySecretKeyProvider(
    algorithm: String = KEY_ALGORITHM_SYMMETRIC,
    override val keySize: Int = KEY_SIZE_SYMMETRIC,
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(algorithm)
) : SecretKeyProvider {

    override val secretKey: SecretKey by lazy { generateKey() }

    private fun generateKey(): SecretKey = try {
        keyGenerator.init(keySize)
        keyGenerator.generateKey()
    } catch (e: Exception) {
        throw RuntimeException("Error generating key: ${e.message}", e)
    }
}

class StoreSecretKeyProvider(
    private val alias: String = "secured_topic_aes",
    private val provider: String = ANDROID_KEYSTORE,
    algorithm: String = KEY_ALGORITHM_SYMMETRIC,
    override val keySize: Int = KEY_SIZE_SYMMETRIC,
    private val keyStore: KeyStore = KeyStore.getInstance(provider),
    private val keyGenParams: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build(),
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(algorithm, provider)
) : SecretKeyProvider {

    override val secretKey: SecretKey by lazy { retrieveKey() ?: generateKey() }

    private fun retrieveKey(): SecretKey? = try {
        keyStore.load(null)
        if (keyStore.containsAlias(alias)) keyStore.getKey(alias, null) as? SecretKey else null
    } catch (e: Exception) {
        throw RuntimeException(
            "Error retrieving secret key ($alias) from $provider : ${e.message}", e
        )
    }

    private fun generateKey(): SecretKey = try {
        keyGenerator.init(keyGenParams)
        keyGenerator.generateKey()
    } catch (e: Exception) {
        throw RuntimeException(
            "Error generating secret key ($alias) from $provider : ${e.message}", e
        )
    }
}
