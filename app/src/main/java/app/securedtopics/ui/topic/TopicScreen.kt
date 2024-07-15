package app.securedtopics.ui.topic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.securedtopics.data.model.Topic
import app.securedtopics.ui.NavErrorScreen
import app.securedtopics.ui.Screen
import app.securedtopics.ui.common.BasicAppBar
import app.securedtopics.ui.common.BasicButton

@Composable
fun TopicScreen(
    viewModel: TopicViewModel = hiltViewModel(),
    onNav: (String) -> Unit
) {
    val topicUi by viewModel.uiState.collectAsState()

    val topic = topicUi.topic
    if (topic == null) NavErrorScreen(message = "Topic not found.", onNav = onNav)
    else TopicContent(topic, onNav = onNav)
}

@Composable
fun TopicContent(
    topic: Topic,
    onNav: ((String) -> Unit)? = null
) {
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            BasicAppBar(
                title = topic.name,
                onBack = { onNav?.invoke(Screen.Back.route) }
            )
        }
    ) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (onNav != null) BasicButton(
                    title = "Export Topic",
                    icon = Icons.Filled.ImportExport,
                    onClick = { onNav("${Screen.ExportTopic.route}/${topic.id}") }
                )
            }
        }
    }
}

@Preview
@Composable
private fun TopicContentPreview() {
    TopicContent(topic = Topic("Hello", "ax", "1"))
}
