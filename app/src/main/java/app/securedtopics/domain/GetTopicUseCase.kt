package app.securedtopics.domain

import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTopicUseCase @Inject constructor(
    private val topicRepo: TopicRepo
) {
    operator fun invoke(topicId: String?): Flow<Topic?> = flow {
        val topic = topicId?.let { topicRepo.getTopic(it) }
        if (topic != null) emit(topic)
    }
}