package com.example.taskmate

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateTaskStatus(
        taskId: Int,
        completed: Boolean
    )

    @Query("SELECT * FROM tasks WHERE isCompleted = :completed ORDER BY id DESC")
    fun getTasksByStatus(completed: Boolean): Flow<List<Task>>
}