package app.securedtopics.crypto.key

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import app.securedtopics.utils.base64decode
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

interface KeyPairProvider {
    val keyPair: KeyPair

    companion object {
        fun fromEncoded(
            encodedPublicKey: String,
            encodedPrivateKey: String? = null,
            algorithm: String = KEY_ALGORITHM_ASYMMETRIC
        ): KeyPairProvider =
            EncodedKeyPairProvider(encodedPublicKey, encodedPrivateKey, algorithm)
    }
}

class EncodedKeyPairProvider(
    private val encodedPublicKey: String,
    private val encodedPrivateKey: String? = null,
    private val algorithm: String = KEY_ALGORITHM_ASYMMETRIC
) : KeyPairProvider {

    override val keyPair: KeyPair by lazy { KeyPair(parsePublic(), parsePrivate()) }

    private fun parsePublic(): PublicKey = try {
        KeyFactory.getInstance(algorithm)
            .generatePublic(X509EncodedKeySpec(encodedPublicKey.base64decode))
    } catch (e: Exception) {
        throw RuntimeException("Error parsing public key: ${e.message}", e)
    }

    private fun parsePrivate(): PrivateKey? = try {
        if (encodedPrivateKey == null) null else KeyFactory.getInstance(algorithm)
            .generatePrivate(PKCS8EncodedKeySpec(encodedPrivateKey.base64decode))
    } catch (e: Exception) {
        throw RuntimeException("Error parsing private key: ${e.message}", e)
    }

}

class RandomKeyPairProvider(
    algorithm: String = KEY_ALGORITHM_ASYMMETRIC,
    private val keySize: Int = KEY_SIZE_ASYMMETRIC,
    private val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(algorithm),
) : KeyPairProvider {

    override val keyPair: KeyPair by lazy { generateKeyPair() }
    private fun generateKeyPair() = try {
        keyPairGenerator.initialize(keySize)
        keyPairGenerator.generateKeyPair()
    } catch (e: Exception) {
        throw RuntimeException("Error generating keypair: ${e.message}", e)
    }
}

class StoreKeyPairProvider(
    private val alias: String = "secured_topic_rsa",
    algorithm: String = KEY_ALGORITHM_ASYMMETRIC,
    provider: String = ANDROID_KEYSTORE,
    private val keyGenParamSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias, KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN
    ).setDigests(
        KeyProperties.DIGEST_SHA1,
        KeyProperties.DIGEST_SHA256,
        KeyProperties.DIGEST_SHA512
    ).setEncryptionPaddings(
        KeyProperties.ENCRYPTION_PADDING_NONE,
        KeyProperties.ENCRYPTION_PADDING_RSA_OAEP,
        KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1,
    ).setSignaturePaddings(
        KeyProperties.SIGNATURE_PADDING_RSA_PKCS1,
        KeyProperties.SIGNATURE_PADDING_RSA_PSS
    ).setBlockModes(KeyProperties.BLOCK_MODE_ECB)
        .setRandomizedEncryptionRequired(true)
        .build(),
    private val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(
        algorithm, provider
    ),
    private val keyStore: KeyStore = KeyStore.getInstance(provider),
) : KeyPairProvider {

    override val keyPair: KeyPair by lazy { retrieveKeyPair() ?: generateKeyPair() }

    private fun retrieveKeyPair(): KeyPair? {
        keyStore.load(null)
        if (!keyStore.containsAlias(alias)) return null
        val privateKey = keyStore.getKey(alias, null) as? PrivateKey ?: return null
        val publicKey = keyStore.getCertificate(alias).publicKey ?: return null
        return KeyPair(publicKey, privateKey)
    }

    private fun generateKeyPair() = try {
        keyPairGenerator.initialize(keyGenParamSpec)
        keyPairGenerator.generateKeyPair()
    } catch (e: Exception) {
        throw RuntimeException("Error generating keypair: ${e.message}", e)
    }
}