package app.securedtopics.domain

import app.securedtopics.data.AppUserRepo
import app.securedtopics.data.model.AppUser
import java.util.UUID
import javax.inject.Inject

class SaveAppUserUseCase @Inject constructor(
    private val appUserRepo: AppUserRepo
) {
    suspend operator fun invoke(userName: String) {
        val appUser = AppUser(name = userName, id = UUID.randomUUID().toString())
        appUserRepo.saveAppUser(appUser)
    }
}