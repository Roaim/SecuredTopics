package app.securedtopics.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Topic
import app.securedtopics.domain.SaveTopicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class HomeUiState(
    val topic: List<Topic> = emptyList(),
    val loading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveTopicUseCase: SaveTopicUseCase,
    private val topicRepo: TopicRepo,
) : ViewModel() {

    val topicsUiState: StateFlow<HomeUiState> =
        topicRepo.getTopics().map { list ->
            HomeUiState(list)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeUiState(loading = true))

    fun saveTopic(topicName: String) = viewModelScope.launch {
        val topic = Topic(name = topicName, key = "", id = UUID.randomUUID().toString())
        saveTopicUseCase(topic)
    }

    fun deleteTopic(topic: Topic) = viewModelScope.launch {
        topicRepo.deleteTopic(topic)
    }
}
