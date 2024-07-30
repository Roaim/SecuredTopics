package app.securedtopics.ui.topic

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.securedtopics.data.model.Message
import app.securedtopics.data.model.Topic
import app.securedtopics.ui.NavErrorScreen
import app.securedtopics.ui.Screen
import app.securedtopics.ui.common.BasicAppBar
import app.securedtopics.ui.common.BasicIconButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TopicScreen(
    viewModel: TopicViewModel = hiltViewModel(),
    onNav: (String) -> Unit
) {
    val topicUi by viewModel.uiState.collectAsState()
    val toast = topicUi.toast
    TopicContent(
        topicUi = topicUi,
        onMsgSend = { topic, message ->
            viewModel.saveMessage(topic, message)
        },
        onExportMsg = viewModel::exportMessage,
        onImportMsg = viewModel::importMessage,
        onNav = onNav
    )

    if (toast != null) {
        Toast.makeText(LocalContext.current, toast, Toast.LENGTH_SHORT).show()
        viewModel.clearToast()
    }
}

@Composable
fun TopicContent(
    topicUi: TopicUiState,
    onMsgSend: (Topic, String) -> Unit,
    onExportMsg: (Message) -> Unit,
    onImportMsg: () -> Unit,
    onNav: (String) -> Unit,
) {
    val topic = topicUi.topic
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = topicUi.messages) {
        delay(500)
        if (topicUi.messages.isNotEmpty()) scrollState.scrollToItem(topicUi.messages.size.dec())
    }

    if (topic == null) NavErrorScreen(message = "Topic not found.", onNav = onNav)
    else Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            BasicAppBar(
                title = topic.name,
                onBack = { onNav.invoke(Screen.Back.route) }
            ) {
                BasicIconButton(icon = Icons.Filled.IosShare) {
                    onNav("${Screen.ExportTopic.route}/${topic.id}")
                }
                BasicIconButton(icon = Icons.Filled.ImportExport, onClick = onImportMsg)
            }
        },
        bottomBar = {
            MessageTextField {
                onMsgSend(topic, it)
                scope.launch {
                    delay(500)
                    if (topicUi.messages.isNotEmpty()) scrollState.scrollToItem(topicUi.messages.size.dec())
                }
            }
        }
    ) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = scrollState
            ) {
                items(topicUi.messages) { message ->
                    MessageItem(message) { onExportMsg(message) }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TopicContentPreview() {
    TopicContent(
        TopicUiState(
            topic = Topic("Topic XYZ", "xyz", "abc"),
            messages = listOf(
                Message(
                    id = "abcd",
                    topicId = "abc",
                    sender = "Akkas",
                    content = "Hello World",
                    publishedAt = System.currentTimeMillis(),
                    timestamp = System.currentTimeMillis()
                ),
                Message(
                    id = "abcd",
                    topicId = "abc",
                    sender = "Akkas",
                    content = "Hello World",
                    publishedAt = System.currentTimeMillis(),
                    timestamp = System.currentTimeMillis()
                )
            )
        ), { _, _ -> }, {}, {}, {}
    )
}
