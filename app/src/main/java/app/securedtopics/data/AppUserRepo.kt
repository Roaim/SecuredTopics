package app.securedtopics.data

import app.securedtopics.data.local.AppSettings
import app.securedtopics.data.model.AppUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUserRepo @Inject constructor(
    private val settings: AppSettings
) {
    val appUser: Flow<AppUser?> get() = settings.appUser

    suspend fun appUserName(): String = appUser.firstOrNull()?.name ?: "UnknownUser"

    suspend fun saveAppUser(user: AppUser) = settings.saveAppUser(user)
}