package app.securedtopics.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.securedtopics.ui.common.BasicAppBar
import app.securedtopics.ui.home.HomeScreen
import app.securedtopics.ui.import_topic.ImportTopicScreen
import app.securedtopics.ui.topic.TopicScreen

private const val ARG_TOPIC_ID = "topicId"

enum class Screen(val route: String, var args: String? = null) {
    Home("home"),
    Back("back"),
    ImportTopic("import-topic"),
    Topic("topic"),
}

@Composable
fun SecuredTopicNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(onNav = navController::navigateTo) }
        composable(Screen.ImportTopic.route) { ImportTopicScreen(onNav = navController::navigateTo) }
        composable(
            "${Screen.Topic.route}/{$ARG_TOPIC_ID}",
            arguments = listOf(navArgument(ARG_TOPIC_ID) { type = NavType.StringType })
        ) { backStack ->
            val topicId = backStack.arguments?.getString(ARG_TOPIC_ID)
            if (topicId != null) TopicScreen(topicId, onNav = navController::navigateTo)
            else NavErrorScreen(
                message = "Topic id must be provided", onNav = navController::navigateTo
            )

        }
    }
}

fun NavHostController.navigateTo(route: String) {
    if (route == Screen.Back.route) navigateUp() else navigate(route = route)
}

@Composable
fun NavErrorScreen(message: String, modifier: Modifier = Modifier, onNav: (String) -> Unit) {
    Scaffold(
        topBar = { BasicAppBar("Navigation Error", onBack = { onNav(Screen.Back.route) }) }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            ) {
                Icon(Icons.Default.Warning, "Error")
                Text(message, Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}