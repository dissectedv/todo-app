package com.example.listadetarefas.datasource

import com.example.listadetarefas.model.TaskModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskDataSource {
    private val firestore = Firebase.firestore
    private val collection = firestore.collection("tasks")

    fun getTasks(): Flow<List<TaskModel>> = callbackFlow {
        val listenerRegistration = collection.orderBy("priority", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val tasks = snapshot.toObjects(TaskModel::class.java)
                    trySend(tasks).isSuccess
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun saveTask(task: TaskModel) {
        if (task.id == null) {
            val docRef = collection.document()
            val newTask = task.copy(id = docRef.id)
            docRef.set(newTask).await()
        } else {
            collection.document(task.id).set(task).await()
        }
    }

    suspend fun deleteTask(taskId: String) {
        collection.document(taskId).delete().await()
    }
}