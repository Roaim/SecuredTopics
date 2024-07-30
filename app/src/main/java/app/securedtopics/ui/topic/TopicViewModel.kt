package app.securedtopics.ui.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.securedtopics.data.MessageRepo
import app.securedtopics.data.model.Message
import app.securedtopics.data.model.Topic
import app.securedtopics.domain.CreateMessageUseCase
import app.securedtopics.domain.EncryptTextUseCase
import app.securedtopics.domain.ExportMessageUseCase
import app.securedtopics.domain.GetDecryptedMessagesUseCase
import app.securedtopics.domain.GetTopicUseCase
import app.securedtopics.domain.ImportMessageUseCase
import app.securedtopics.ui.ARG_TOPIC_ID
import app.securedtopics.utils.FlowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TopicUiState(
    val topic: Topic? = null,
    val messages: List<Message> = emptyList(),
    val loading: Boolean = false,
    val toast: String? = null,
)

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTopicUseCase: GetTopicUseCase,
    getDecryptedMessagesUseCase: GetDecryptedMessagesUseCase,
    private val encryptTextUseCase: EncryptTextUseCase,
    private val createMessageUseCase: CreateMessageUseCase,
    private val exportMessageUseCase: ExportMessageUseCase,
    private val importMessageUseCase: ImportMessageUseCase,
    private val messageRepo: MessageRepo,
) : ViewModel(), FlowState {

    private val _topic: Flow<Topic?> = getTopicUseCase(savedStateHandle[ARG_TOPIC_ID])
    private val _refresh = MutableStateFlow(0)
    private val _toast = MutableStateFlow<String?>(null)

    val uiState: StateFlow<TopicUiState> = combine(_topic, _toast, _refresh) { topic, toast, _ ->
        val messages = getDecryptedMessagesUseCase(topic?.id)
        TopicUiState(topic = topic, messages = messages, toast = toast)
    }.stateInWhileSubscribed(viewModelScope, TopicUiState(loading = true))

    fun saveMessage(topic: Topic, message: String) = viewModelScope.launch {
        val encryptedMessage = encryptTextUseCase(topic.key, message) ?: return@launch
        val messageToSave = createMessageUseCase(topicId = topic.id, message = encryptedMessage)
        messageRepo.saveMessage(messageToSave)
        refreshMessages()
    }

    fun exportMessage(message: Message) = viewModelScope.launch {
        exportMessageUseCase(message)
    }

    fun importMessage() = viewModelScope.launch {
        val msg = importMessageUseCase()
        _toast.emit(msg)
        refreshMessages()
    }

    fun clearToast() = _toast.tryEmit(null)

    private fun refreshMessages() = viewModelScope.launch {
        _refresh.emit(_refresh.first().inc())
    }

}
