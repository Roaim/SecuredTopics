package app.securedtopics.crypto.key

import androidx.security.crypto.MasterKeys

object MasterKeyInfo : StoreKeyInfo {
    private val keySpec = MasterKeys.AES256_GCM_SPEC
    override val keyAlias: String = MasterKeys.getOrCreate(keySpec)
}

object MasterKey : CryptoKey by SymmetricStoreKey(MasterKeyInfo)
