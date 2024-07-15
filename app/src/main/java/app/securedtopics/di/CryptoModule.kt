package app.securedtopics.di

import android.security.keystore.KeyGenParameterSpec
import androidx.security.crypto.MasterKeys
import app.securedtopics.crypto.AsymmetricCryptography
import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.KeyPairProvider
import app.securedtopics.crypto.key.SecretKeyProvider
import app.securedtopics.crypto.key.StoreKeyPairProvider
import app.securedtopics.crypto.key.StoreSecretKeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoModule {

    @Provides
    @SymmetricMaster
    fun provideMasterParamSpec(): KeyGenParameterSpec = MasterKeys.AES256_GCM_SPEC

    @Provides
    @SymmetricMaster
    fun provideMasterKey(@SymmetricMaster params: KeyGenParameterSpec): SecretKeyProvider =
        StoreSecretKeyProvider(alias = MasterKeys.getOrCreate(params), keyGenParams = params)

    @Provides
    @SymmetricMaster
    fun provideMasterCryptography(@SymmetricMaster keyProvider: SecretKeyProvider): Cryptography =
        SymmetricCryptography(keyProvider)

    @Provides
    @Singleton
    @AsymmetricStore
    fun provideStoreKeyPair(): KeyPairProvider = StoreKeyPairProvider()

    @Provides
    @AsymmetricStore
    fun provideAsymmetricCryptography(@AsymmetricStore keyProvider: KeyPairProvider): Cryptography =
        AsymmetricCryptography(keyProvider)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SymmetricMaster

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AsymmetricStore