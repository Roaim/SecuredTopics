package app.securedtopics.ui.import_topic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.securedtopics.data.model.Topic
import app.securedtopics.ui.Screen
import app.securedtopics.ui.common.BasicAppBar
import app.securedtopics.ui.common.BasicButton
import app.securedtopics.ui.common.BasicProgress
import app.securedtopics.utils.json

@Composable
fun ImportTopicScreen(
    viewModel: ImportTopicViewModel = hiltViewModel(),
    onNav: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    ImportTopicContent(
        uiState,
        onCopy = viewModel::copy,
        onNav = onNav,
        onImport = viewModel::importTopic
    )
}

@Composable
fun ImportTopicContent(
    uiState: ImportTopicUiState,
    onCopy: ((String) -> Unit)? = null,
    onNav: ((String) -> Unit)? = null,
    onImport: (() -> Unit)? = null
) {

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            BasicAppBar(
                title = "Import Topic",
                onBack = { onNav?.invoke(Screen.Back.route) })
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            if (uiState.loading) BasicProgress() else Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val publicKey = uiState.publicKey
                if (publicKey != null) {
                    Text(text = "Public Key", style = MaterialTheme.typography.titleMedium)
                    Text(text = publicKey, style = MaterialTheme.typography.bodySmall)
                    BasicButton(title = "Copy Public Key", icon = Icons.Filled.ContentCopy) {
                        onCopy?.invoke(publicKey)
                    }
                    BasicButton(title = "Paste Topic", icon = Icons.Filled.ContentPaste) {
                        onImport?.invoke()
                    }
                }
                val topic = uiState.topic
                if (topic != null) Text(text = topic.json)
            }
        }
    }
}

@Preview
@Composable
private fun ImportTopicContentPreview() {
    ImportTopicContent(
        uiState = ImportTopicUiState(
            "Hello World",
            Topic("Topic", "key-1", "uid")
        )
    )
}
