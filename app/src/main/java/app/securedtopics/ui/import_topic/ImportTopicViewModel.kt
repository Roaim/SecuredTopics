package app.securedtopics.ui.import_topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.securedtopics.crypto.key.CryptoKey
import app.securedtopics.data.model.Topic
import app.securedtopics.di.Asymmetric
import app.securedtopics.domain.DecryptTopicUseCase
import app.securedtopics.domain.SaveTopicUseCase
import app.securedtopics.utils.ClipboardService
import app.securedtopics.utils.base64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImportTopicUiState(
    val publicKey: String? = null,
    val topic: Topic? = null,
    val loading: Boolean = false,
)

@HiltViewModel
class ImportTopicViewModel @Inject constructor(
    @Asymmetric private val cryptoKey: CryptoKey,
    private val clipboardService: ClipboardService,
    private val decryptTopicUseCase: DecryptTopicUseCase,
    private val saveTopicUseCase: SaveTopicUseCase,
) : ViewModel() {

    private val _publicKey = MutableStateFlow<String?>(null)
    private val _topic = MutableStateFlow<Topic?>(null)

    val uiState: StateFlow<ImportTopicUiState> = _publicKey.combine(_topic) { pubKey, topic ->
        ImportTopicUiState(publicKey = pubKey, topic = topic)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ImportTopicUiState(loading = true)
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            _publicKey.emit(cryptoKey.encryptKey.encoded.base64)
        }
    }

    fun copy(pubKey: String) {
        clipboardService.copyToClipboard(pubKey, "PublicKey")
    }

    fun importTopic() {
        val topic = decryptTopicUseCase() ?: return
        viewModelScope.launch {
            val savedTopic = saveTopicUseCase(topic)
            _topic.emit(savedTopic)
        }
    }

}