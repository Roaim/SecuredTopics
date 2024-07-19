package app.securedtopics.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.securedtopics.ui.common.BasicAppBar
import app.securedtopics.ui.common.BasicButton
import app.securedtopics.ui.theme.SecuredTopicsTheme

@Composable
fun AppUserScreen(
    onCreateUser: (username: String) -> Unit,
) {
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            BasicAppBar(title = "App User")
        }
    ) {
        AppUserContent(Modifier.padding(it), onCreateUser)
    }
}

@Composable
fun AppUserContent(
    modifier: Modifier = Modifier,
    onCreateUser: (username: String) -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Who is gonna use the app?")

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username") }
        )

        BasicButton(title = "Continue", icon = Icons.AutoMirrored.Default.Login) {
            if (username.isNotBlank()) onCreateUser(username)
        }

        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Preview
@Composable
private fun AppUserContentPreview() {
    SecuredTopicsTheme {
        AppUserScreen {}
    }
}
