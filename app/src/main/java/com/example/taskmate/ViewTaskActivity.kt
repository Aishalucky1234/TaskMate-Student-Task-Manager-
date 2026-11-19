package com.example.taskmate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmate.ui.theme.TaskMateTheme

class ViewTasksActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskDao = TaskDatabase
            .getDatabase(applicationContext)
            .taskDao()

        setContent {
            TaskMateTheme {
                val tasks by taskDao.getAllTasks()
                    .collectAsState(initial = emptyList())

                ViewTasksScreen(
                    tasks = tasks,
                    onBackClick = {
                        finish()
                    },
                    onTaskClick = { task ->
                        val intent = Intent(
                            this@ViewTasksActivity,
                            TaskDetailsActivity::class.java
                        )

                        intent.putExtra("TASK_ID", task.id)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun ViewTasksScreen(
    tasks: List<Task>,
    onBackClick: () -> Unit,
    onTaskClick: (Task) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onBackClick
        ) {
            Text("← Back to Dashboard")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tasks.isEmpty()) {
            Text("No tasks available")
        } else {
            LazyColumn {
                items(
                    items = tasks,
                    key = { it.id }
                ) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { onTaskClick(task) }
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