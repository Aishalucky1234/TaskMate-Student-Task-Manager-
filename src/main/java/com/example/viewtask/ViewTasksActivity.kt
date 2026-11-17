package com.example.viewtask


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class ViewTasksActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskDao = TaskDatabase
            .getDatabase(applicationContext)
            .taskDao()

        setContent {
            val tasks by taskDao.getAllTasks()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

            ViewTasksScreen(tasks)
        }
    }
}

@Composable
fun ViewTasksScreen(tasks: List<Task>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tasks.isEmpty()) {

            Text("No tasks available")

        } else {

            LazyColumn {

                items(tasks) { task ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Text(text = task.description)

                            Text(text = "Deadline: ${task.deadline}")

                            Text(
                                text = if (task.isCompleted)
                                    "Completed"
                                else
                                    "Pending"
                            )
                        }
                    }
                }
            }
        }
    }
}