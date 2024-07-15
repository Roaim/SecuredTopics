package app.securedtopics.di

import app.securedtopics.crypto.AsymmetricCryptography
import app.securedtopics.crypto.Cryptography
import app.securedtopics.crypto.SymmetricCryptography
import app.securedtopics.crypto.key.AsymmetricStoreKey
import app.securedtopics.crypto.key.AsymmetricStoreKeyInfo
import app.securedtopics.crypto.key.CryptoKey
import app.securedtopics.crypto.key.MasterKey
import app.securedtopics.crypto.key.StoreKeyInfo
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
    @Singleton
    @Asymmetric
    fun provideAsymmetricKeyInfo(): StoreKeyInfo = AsymmetricStoreKeyInfo()

    @Provides
    @SymmetricMaster
    fun provideMasterKey(): CryptoKey = MasterKey

    @Provides
    @Singleton
    @Asymmetric
    fun provideImportTopicKey(@Asymmetric info: StoreKeyInfo): CryptoKey = AsymmetricStoreKey(info)

    @Provides
    @SymmetricMaster
    fun provideMasterCryptography(@SymmetricMaster cryptoKey: CryptoKey): Cryptography =
        SymmetricCryptography(cryptoKey)

    @Provides
    @Asymmetric
    fun provideAsymmetricCryptography(@Asymmetric cryptoKey: CryptoKey): Cryptography =
        AsymmetricCryptography(cryptoKey)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SymmetricMaster

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Asymmetric