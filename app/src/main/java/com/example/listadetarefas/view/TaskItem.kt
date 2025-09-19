package com.example.listadetarefas.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.listadetarefas.model.TaskModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(task: TaskModel, onDelete: () -> Unit, onClick: () -> Unit) {
    val priorityMap = when (task.priority) {
        1 -> "Baixa" to Color(0xFF4CAF50)
        2 -> "Média" to Color(0xFFFFC107)
        3 -> "Alta" to Color(0xFFF44336)
        else -> "Nenhuma" to Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            val (title, desc, priorityLabel, priorityColor, deleteButton, locationIcon, locationText) = createRefs()

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(deleteButton.start, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.constrainAs(desc) {
                    top.linkTo(title.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(deleteButton.start, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = "Prioridade: ${priorityMap.first}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(priorityLabel) {
                    top.linkTo(desc.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
            )

            Surface(
                shape = CircleShape,
                color = priorityMap.second,
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(priorityColor) {
                        start.linkTo(priorityLabel.end, margin = 8.dp)
                        centerVerticallyTo(priorityLabel)
                    }
            ) {}

            if (task.location.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Ícone de Localização",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(18.dp)
                        .constrainAs(locationIcon) {
                            start.linkTo(priorityColor.end, margin = 16.dp)
                            centerVerticallyTo(priorityLabel)
                        }
                )
                Text(
                    text = task.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.constrainAs(locationText) {
                        start.linkTo(locationIcon.end, margin = 4.dp)
                        centerVerticallyTo(priorityLabel)
                        end.linkTo(deleteButton.start, margin = 8.dp)
                        width = Dimension.preferredWrapContent
                    }
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.constrainAs(deleteButton) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}