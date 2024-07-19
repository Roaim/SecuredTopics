package app.securedtopics.di

import app.securedtopics.data.local.AppSettings
import app.securedtopics.data.local.PrefAppSettings
import app.securedtopics.utils.AndroidClipboardService
import app.securedtopics.utils.ClipboardService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModule {

    @Binds
    abstract fun bindClipboardService(clipboardService: AndroidClipboardService): ClipboardService

    @Binds
    @Singleton
    abstract fun bindAppSettings(settings: PrefAppSettings): AppSettings

}
