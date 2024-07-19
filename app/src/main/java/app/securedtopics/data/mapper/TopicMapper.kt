package app.securedtopics.data.mapper

import app.securedtopics.data.local.model.LocalTopic
import app.securedtopics.data.model.Topic

val Topic.asLocal: LocalTopic get() = LocalTopic(name = name, key = key, id = id)
val LocalTopic.asExternal: Topic get() = Topic(name = name, key = key, id = id)
