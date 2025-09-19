package com.example.listadetarefas.repository

import com.example.listadetarefas.datasource.TaskDataSource
import com.example.listadetarefas.model.TaskModel
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dataSource: TaskDataSource = TaskDataSource()) {
    fun getTasks(): Flow<List<TaskModel>> = dataSource.getTasks()
    suspend fun saveTask(task: TaskModel) = dataSource.saveTask(task)
    suspend fun deleteTask(taskId: String) = dataSource.deleteTask(taskId)
}