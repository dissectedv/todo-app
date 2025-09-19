package com.example.listadetarefas.model

data class TaskModel(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val priority: Int = 0
)
