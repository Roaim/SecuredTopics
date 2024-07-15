package app.securedtopics.ui.topic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    topicId: String,
    viewModel: TopicViewModel = hiltViewModel(),
    onNav: (String) -> Unit
) {
    val topicUi by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = topicId) {
        viewModel.setTopicId(topicId)
    }
    val topic = topicUi.topic
    if (topic == null) NavErrorScreen(message = "Topic with id: $topicId not found.", onNav = onNav)
    else TopicContent(topic, onNav = onNav, onExportTopic = viewModel::exportTopic)
}

@Composable
fun TopicContent(
    topic: Topic,
    onExportTopic: (() -> Unit)? = null,
    onNav: ((String) -> Unit)? = null
) {
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            BasicAppBar(
                title = "Topic: ${topic.name}",
                onBack = { onNav?.invoke(Screen.Back.route) })
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
                Text(text = "Topic Info", style = MaterialTheme.typography.titleMedium)
                Text(text = topic.id)
                Text(text = topic.name)
                Text(text = topic.key)
                if (onExportTopic != null) BasicButton(
                    title = "Export Topic",
                    icon = Icons.Filled.ImportExport,
                    onClick = onExportTopic
                )
            }
        }
    }
}

@Preview
@Composable
private fun TopicContentPreview() {
    TopicContent(topic = Topic("Hello", "ax", "1"), onExportTopic = {})
}
