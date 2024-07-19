package app.securedtopics.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.securedtopics.data.AppUserRepo
import app.securedtopics.data.model.AppUser
import app.securedtopics.domain.SaveAppUserUseCase
import app.securedtopics.utils.FlowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppUserViewModel @Inject constructor(
    private val saveAppUserUseCase: SaveAppUserUseCase,
    appUserRepo: AppUserRepo
) : ViewModel(), FlowState {

    val appUser: StateFlow<AppUser?> = appUserRepo.appUser.stateInLazily(viewModelScope, null)

    fun saveAppUser(userName: String) = viewModelScope.launch {
        saveAppUserUseCase(userName)
    }

}