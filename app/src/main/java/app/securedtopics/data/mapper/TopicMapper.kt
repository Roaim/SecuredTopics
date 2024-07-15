package app.securedtopics.data.mapper

import app.securedtopics.data.local.model.LocalTopic
import app.securedtopics.data.model.Topic

val Topic.asLocal: LocalTopic get() = LocalTopic(name, key, id)
val LocalTopic.asExternal: Topic get() = Topic(name, key, id)
