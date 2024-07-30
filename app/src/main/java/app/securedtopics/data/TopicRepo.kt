package app.securedtopics.data

import app.securedtopics.data.local.dao.TopicDao
import app.securedtopics.data.mapper.asExternal
import app.securedtopics.data.mapper.asLocal
import app.securedtopics.data.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicRepo @Inject constructor(private val topicDao: TopicDao) {

    fun getTopics(): Flow<List<Topic>> = topicDao.getAll().map { list ->
        list.map { it.asExternal }
    }

    suspend fun getTopic(id: String): Topic? = topicDao.getById(id)?.asExternal

    suspend fun saveTopic(topic: Topic) {
        topicDao.save(topic.asLocal)
    }

    suspend fun deleteTopic(topic: Topic) {
        topicDao.delete(topic.asLocal)
    }

}