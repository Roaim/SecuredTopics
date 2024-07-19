package app.securedtopics.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import app.securedtopics.data.model.AppUser
import app.securedtopics.di.EncryptedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefAppSettings @Inject constructor(
    private val pref: SharedPreferences,
    @EncryptedPref private val encryptedPref: SharedPreferences,
) : AppSettings {

    companion object {
        private const val KEY_USER_ID = "userid"
        private const val KEY_USER_NAME = "username"
    }

    private val _appUser = MutableStateFlow<AppUser?>(null)

    override val appUser: Flow<AppUser?>
        get() = _appUser.map { user ->
            if (user == null) {
                emitAppUser()
                null
            } else user
        }

    override suspend fun saveAppUser(user: AppUser) = withContext(Dispatchers.IO) {
        pref.edit {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
        }
        _appUser.emit(user)
    }

    private suspend fun emitAppUser() = withContext(Dispatchers.IO) {
        val userId = pref.getString(KEY_USER_ID, null) ?: return@withContext
        val userName = pref.getString(KEY_USER_NAME, null) ?: return@withContext
        val appUser = AppUser(id = userId, name = userName)
        _appUser.emit(appUser)
    }

}

interface AppSettings {
    val appUser: Flow<AppUser?>
    suspend fun saveAppUser(user: AppUser)
}
