package com.example.listadetarefas.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.listadetarefas.components.CustomButton
import com.example.listadetarefas.components.MyTextField
import com.example.listadetarefas.model.TaskModel
import com.example.listadetarefas.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel,
    taskToEdit: TaskModel?
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(taskToEdit?.description ?: "") }
    var location by remember { mutableStateOf(taskToEdit?.location ?: "") }
    var selectedPriority by remember { mutableIntStateOf(taskToEdit?.priority ?: 0) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskToEdit == null) "Salvar Tarefa" else "Editar Tarefa") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF212121),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            MyTextField(
                value = title,
                onValueChange = { title = it },
                label = "Título da tarefa"
            )
            Spacer(modifier = Modifier.height(16.dp))
            MyTextField(
                value = description,
                onValueChange = { description = it },
                label = "Descrição",
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(16.dp))
            MyTextField(
                value = location,
                onValueChange = { location = it },
                label = "Local"
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Nível de prioridade", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            PrioritySelector(
                selectedPriority = selectedPriority,
                onPrioritySelected = { selectedPriority = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomButton(
                        onClick = { navController.popBackStack() },
                        text = "Cancelar",
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    CustomButton(
                        onClick = {
                            if (title.isBlank() || description.isBlank()) {
                                Toast.makeText(context, "Título e Descrição são obrigatórios.", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.saveTask(title, description, location, selectedPriority, taskToEdit?.id)
                                navController.popBackStack()
                            }
                        },
                        text = "Salvar",
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PrioritySelector(
    selectedPriority: Int,
    onPrioritySelected: (Int) -> Unit
) {
    val priorities = listOf(
        1 to Color(0xFF4CAF50),
        2 to Color(0xFFFFC107),
        3 to Color(0xFFF44336)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        // O alinhamento foi alterado para Center para centralizar os botões
        horizontalArrangement = Arrangement.Center
    ) {
        priorities.forEach { (priorityLevel, color) ->
            RadioButton(
                selected = (selectedPriority == priorityLevel),
                onClick = { onPrioritySelected(priorityLevel) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = color.copy(alpha = 0.5f)
                )
            )
            // Mantém um espaço entre os botões
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}