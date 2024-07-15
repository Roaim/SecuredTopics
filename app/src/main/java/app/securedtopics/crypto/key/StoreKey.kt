package app.securedtopics.crypto.key

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.BLOCK_MODE_ECB
import android.security.keystore.KeyProperties.DIGEST_SHA1
import android.security.keystore.KeyProperties.DIGEST_SHA256
import android.security.keystore.KeyProperties.DIGEST_SHA512
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_OAEP
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
import android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_SIGN
import android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PKCS1
import android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PSS
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore

interface StoreKeyInfo {
    val providerName: String get() = ANDROID_KEYSTORE
    val keyAlias: String

    companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
    }
}

open class SymmetricStoreKey(
    private val info: StoreKeyInfo,
    override val size: Int = 128,
) : CryptoKey {

    private val key: Key by lazy {
        KeyStore.getInstance(info.providerName).run {
            load(null)
            getKey(info.keyAlias, null)
        }
    }
    override val encryptKey: Key get() = key
    override val decryptKey: Key get() = key

}

class AsymmetricStoreKeyInfo(
    algorithm: String = KEY_ALGORITHM_RSA,
    override val keyAlias: String = "rsa",
) : StoreKeyInfo {
    init {
        KeyPairGenerator.getInstance(algorithm, providerName).apply {
            initialize(
                KeyGenParameterSpec.Builder(keyAlias, PURPOSE_DECRYPT or PURPOSE_SIGN)
                    .setDigests(DIGEST_SHA1, DIGEST_SHA256, DIGEST_SHA512)
                    .setEncryptionPaddings(
                        ENCRYPTION_PADDING_NONE,
                        ENCRYPTION_PADDING_RSA_OAEP,
                        ENCRYPTION_PADDING_RSA_PKCS1
                    )
                    .setSignaturePaddings(SIGNATURE_PADDING_RSA_PKCS1, SIGNATURE_PADDING_RSA_PSS)
                    .setBlockModes(BLOCK_MODE_ECB)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
            generateKeyPair()
        }
    }
}

open class AsymmetricStoreKey(
    private val info: StoreKeyInfo,
    override val size: Int = 1024,
) : CryptoKey {
    private val keyStore by lazy {
        KeyStore.getInstance(info.providerName).apply { load(null) }
    }

    override val encryptKey: Key get() = keyStore.getCertificate(info.keyAlias).publicKey
    override val decryptKey: Key get() = keyStore.getKey(info.keyAlias, null)
}
