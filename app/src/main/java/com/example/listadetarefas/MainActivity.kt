package com.example.listadetarefas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listadetarefas.datastore.ThemeDataStore
import com.example.listadetarefas.model.TaskModel
import com.example.listadetarefas.ui.theme.ListaDeTarefasTheme
import com.example.listadetarefas.view.SaveTaskScreen
import com.example.listadetarefas.view.TaskListScreen
import com.example.listadetarefas.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeDataStore = ThemeDataStore(LocalContext.current)
            val isDarkTheme by themeDataStore.isDarkTheme.collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            ListaDeTarefasTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = {
                            scope.launch {
                                themeDataStore.saveTheme(!isDarkTheme)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val viewModel: TaskViewModel = viewModel()

    val onEditTask = { task: TaskModel ->
        val encodedTitle = URLEncoder.encode(task.title, "UTF-8")
        val encodedDesc = URLEncoder.encode(task.description, "UTF-8")
        val encodedLocation = URLEncoder.encode(task.location, "UTF-8")
        navController.navigate("saveTask?id=${task.id}&title=${encodedTitle}&desc=${encodedDesc}&location=${encodedLocation}&priority=${task.priority}")
    }

    NavHost(navController = navController, startDestination = "taskList") {
        composable("taskList") {
            // CORREÇÃO: Passar os parâmetros isDarkTheme e onThemeToggle aqui
            TaskListScreen(
                navController = navController,
                viewModel = viewModel,
                onEditTask = onEditTask,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
        composable(
            route = "saveTask?id={id}&title={title}&desc={desc}&location={location}&priority={priority}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType; nullable = true },
                navArgument("title") { type = NavType.StringType; nullable = true },
                navArgument("desc") { type = NavType.StringType; nullable = true },
                navArgument("location") { type = NavType.StringType; nullable = true },
                navArgument("priority") { type = NavType.IntType; defaultValue = 0 }
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val id = arguments?.getString("id")
            val title = arguments?.getString("title")?.let { URLDecoder.decode(it, "UTF-8") }
            val desc = arguments?.getString("desc")?.let { URLDecoder.decode(it, "UTF-8") }
            val location = arguments?.getString("location")?.let { URLDecoder.decode(it, "UTF-8") }
            val priority = arguments?.getInt("priority")

            val taskToEdit = if (id != null && title != null && desc != null && location != null && priority != null) {
                TaskModel(id, title, desc, location, priority)
            } else {
                null
            }

            SaveTaskScreen(navController = navController, viewModel = viewModel, taskToEdit = taskToEdit)
        }
    }
}