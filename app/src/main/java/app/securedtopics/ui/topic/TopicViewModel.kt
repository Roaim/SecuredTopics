package app.securedtopics.ui.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.securedtopics.data.model.Topic
import app.securedtopics.domain.GetTopicUseCase
import app.securedtopics.ui.ARG_TOPIC_ID
import app.securedtopics.utils.FlowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class TopicUiState(
    val topic: Topic? = null,
    val loading: Boolean = false
)

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTopicUseCase: GetTopicUseCase,
) : ViewModel(), FlowState {

    private val _topic: Flow<Topic?> = getTopicUseCase(savedStateHandle[ARG_TOPIC_ID])

    val uiState: StateFlow<TopicUiState> = _topic.map { TopicUiState(it) }
        .stateInWhileSubscribed(viewModelScope, TopicUiState(loading = false))

}
