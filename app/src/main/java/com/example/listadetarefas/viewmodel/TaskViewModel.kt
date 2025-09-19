package com.example.listadetarefas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listadetarefas.model.TaskModel
import com.example.listadetarefas.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository = TaskRepository()) : ViewModel() {

    val tasks: StateFlow<List<TaskModel>> = repository.getTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveTask(title: String, description: String, location: String, priority: Int, id: String? = null) {
        viewModelScope.launch {
            if (title.isNotBlank() && description.isNotBlank()) {
                val task = TaskModel(
                    id = id,
                    title = title,
                    description = description,
                    location = location,
                    priority = priority
                )
                repository.saveTask(task)
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }
}