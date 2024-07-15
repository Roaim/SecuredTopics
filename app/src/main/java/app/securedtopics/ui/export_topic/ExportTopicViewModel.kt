package app.securedtopics.ui.export_topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.securedtopics.data.model.Topic
import app.securedtopics.domain.ExportTopicUseCase
import app.securedtopics.domain.GetTopicUseCase
import app.securedtopics.ui.ARG_TOPIC_ID
import app.securedtopics.utils.FlowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExportTopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTopicUseCase: GetTopicUseCase,
    private val exportTopicUseCase: ExportTopicUseCase,
) : ViewModel(), FlowState {

    private val _topic: Flow<Topic?> = getTopicUseCase(savedStateHandle[ARG_TOPIC_ID])

    val uiState: StateFlow<ExportTopicUiState> = _topic.map { ExportTopicUiState(it) }
        .stateInWhileSubscribed(viewModelScope, ExportTopicUiState(loading = false))

    fun exportTopic() {
        viewModelScope.launch {
            val topic = _topic.firstOrNull() ?: return@launch
            exportTopicUseCase(topic)
        }
    }
}
