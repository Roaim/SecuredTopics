package app.securedtopics.ui.topic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.securedtopics.data.model.Message
import app.securedtopics.data.model.publishedAtStr

@Composable
fun MessageTextField(modifier: Modifier = Modifier, onSend: (String) -> Unit) {
    var msgTxt by rememberSaveable { mutableStateOf("") }

    Row(
        modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = msgTxt,
            onValueChange = { msgTxt = it },
            placeholder = { Text("Type message") },
        )
        IconButton(onClick = {
            if (msgTxt.isNotBlank()) {
                onSend(msgTxt)
                msgTxt = ""
            }
        }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun MessageItem(
    message: Message,
    modifier: Modifier = Modifier,
    onExport: () -> Unit
) {
    Card(modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = message.sender,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.DarkGray
                    )
                    Text(text = message.publishedAtStr, style = MaterialTheme.typography.labelSmall)
                }
                Text(text = message.content, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onExport) {
                Icon(
                    imageVector = Icons.Filled.IosShare,
                    contentDescription = "Share"
                )
            }
        }
    }
}

@Preview
@Composable
private fun MessageFieldPreview() {
    MessageTextField {}
}

@Preview
@Composable
private fun MessageItemPreview() {
    MessageItem(
        message = Message(
            id = "abcd",
            topicId = "abc",
            sender = "Akkas",
            content = "Hello World",
            publishedAt = System.currentTimeMillis(),
            timestamp = System.currentTimeMillis()
        )
    ) {}
}