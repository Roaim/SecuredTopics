package app.securedtopics.domain

import app.securedtopics.data.TopicRepo
import app.securedtopics.data.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetTopicUseCase @Inject constructor(
    private val topicRepo: TopicRepo
) {
    operator fun invoke(topicId: String?): Flow<Topic?> =
        topicId?.let(topicRepo::getTopic) ?: flowOf()
}