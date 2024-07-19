package app.securedtopics.data

import app.securedtopics.data.local.AppSettings
import app.securedtopics.data.model.AppUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUserRepo @Inject constructor(
    private val settings: AppSettings
) {
    val appUser: Flow<AppUser?> get() = settings.appUser

    suspend fun saveAppUser(user: AppUser) = settings.saveAppUser(user)
}