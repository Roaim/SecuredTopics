package app.securedtopics.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Topic
import app.securedtopics.domain.ExportTopicUseCase
import app.securedtopics.utils.FlowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TopicUiState(
    val topic: Topic? = null,
    val loading: Boolean = false
)

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val exportTopicUseCase: ExportTopicUseCase,
    private val topicRepo: TopicRepo,
) : ViewModel(), FlowState {

    private val _topicId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _topic: Flow<Topic?> = _topicId.flatMapLatest { id ->
        id?.let(topicRepo::getTopic) ?: flowOf()
    }

    val uiState: StateFlow<TopicUiState> = _topic.map { TopicUiState(it) }
        .stateInWhileSubscribed(viewModelScope, TopicUiState(loading = false))

    fun setTopicId(id: String) = viewModelScope.launch {
        _topicId.emit(id)
    }

    fun exportTopic() {
        viewModelScope.launch {
            val topic = _topic.firstOrNull() ?: return@launch
            exportTopicUseCase(topic)
        }
    }

}
