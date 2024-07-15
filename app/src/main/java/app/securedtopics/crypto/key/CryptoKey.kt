package app.securedtopics.crypto.key

import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA
import app.securedtopics.utils.base64decode
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

interface CryptoKey {
    val size: Int
    val encryptKey: Key
    val decryptKey: Key?
}

class SymmetricKey(
    private val algorithm: String = KEY_ALGORITHM_AES,
    override val size: Int = 128,
) : CryptoKey {

    private val key: Key by lazy {
        KeyGenerator.getInstance(algorithm).apply { init(size) }.generateKey()
    }

    override val encryptKey: Key get() = key
    override val decryptKey: Key get() = key

    object Generator {
        fun generate(algorithm: String = KEY_ALGORITHM_AES, size: Int = 128): CryptoKey =
            SymmetricKey(algorithm, size)
    }
}

class AsymmetricKey(
    private val algorithm: String = KEY_ALGORITHM_RSA,
    override val size: Int = 1024,
) : CryptoKey {
    private val keyPair by lazy {
        KeyPairGenerator.getInstance(algorithm).apply { initialize(size) }.generateKeyPair()
    }
    override val encryptKey: Key get() = keyPair.public
    override val decryptKey: Key? get() = keyPair.private

    object Generator {
        fun generate(algorithm: String = KEY_ALGORITHM_RSA, size: Int = 1024): CryptoKey =
            AsymmetricKey(algorithm, size)
    }
}

class PublicKey(
    private val publicKeyEncoded: String,
    private val algorithm: String = KEY_ALGORITHM_RSA,
    override val size: Int = 1024,
    override val decryptKey: Key? = null,
) : CryptoKey {
    private val publicKey by lazy {
        KeyFactory.getInstance(algorithm)
            .generatePublic(X509EncodedKeySpec(publicKeyEncoded.base64decode))
    }
    override val encryptKey: Key get() = publicKey
}

class SecretKey(
    private val secretKey: ByteArray,
    private val algorithm: String = KEY_ALGORITHM_AES,
    override val size: Int = 128,
): CryptoKey {

    private val _secretKey by lazy {
        SecretKeySpec(secretKey, algorithm)
    }

    override val encryptKey: Key get() = _secretKey
    override val decryptKey: Key? get() = _secretKey
}
