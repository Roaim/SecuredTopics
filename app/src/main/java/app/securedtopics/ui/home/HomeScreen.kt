package app.securedtopics.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.securedtopics.R
import app.securedtopics.data.model.Topic
import app.securedtopics.ui.Screen
import app.securedtopics.ui.common.BasicAppBar
import app.securedtopics.ui.common.BasicButton
import app.securedtopics.ui.common.BasicProgress
import app.securedtopics.ui.theme.SecuredTopicsTheme

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onNav: (String) -> Unit) {
    val topicUiState by viewModel.topicsUiState.collectAsState()
    HomeContent(
        topicUiState,
        onAddTopic = viewModel::saveTopic,
        onImport = { onNav(Screen.ImportTopic.route) },
        onNav = onNav
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onAddTopic: ((String) -> Unit)? = null,
    onImport: (() -> Unit)? = null,
    onNav: ((String) -> Unit)? = null,
) {
    var addTopic by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        BasicAppBar(stringResource(R.string.home), actions = {
            if (onImport != null) IconButton(onClick = onImport) {
                Icon(imageVector = Icons.Rounded.ImportExport, contentDescription = "Import Topic")
            }
        })
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (uiState.loading) BasicProgress() else LazyColumn(
                Modifier.padding(horizontal = 8.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.topic) { topic ->
                    TopicItem(topic) { onNav?.invoke("${Screen.Topic.route}/${topic.id}") }
                }
                item {
                    BasicButton(title = "Add Topic") { addTopic = true }
                }
            }
        }
    }

    if (addTopic) AddTopicDialog(
        onDismiss = { addTopic = false },
        onAdd = { topicName -> onAddTopic?.invoke(topicName) }
    )
}

@Composable
fun AddTopicDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            BasicButton(title = "Save") {
                if (name.isNotBlank()) {
                    onAdd(name)
                    onDismiss()
                }
            }
        },
        title = { Text(text = "Save Topic") },
        text = {
            Text("")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Topic Name") },
                singleLine = true,
                isError = name.isBlank()
            )
        }
    )
}

@Composable
fun TopicItem(topic: Topic, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, modifier.fillMaxWidth()) {
        Text(text = topic.name, modifier = Modifier.padding(8.dp))
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    SecuredTopicsTheme {
        HomeContent(
            HomeUiState(
                listOf(
                    Topic("Jata", "xyz", "1"),
                    Topic("Chata", "abc", "2"),
                )
            )
        )
    }
}
