package app.securedtopics.ui

import androidx.compose.runtime.Composable
import app.securedtopics.ui.theme.SecuredTopicsTheme

@Composable
fun SecuredTopicsApp() {
    SecuredTopicsTheme {
        SecuredTopicNavHost()
    }
}