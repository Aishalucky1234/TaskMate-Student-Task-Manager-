package com.example.viewtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class TaskDetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskId = intent.getIntExtra("TASK_ID", -1)

        lifecycleScope.launch {

            val task = TaskDatabase
                .getDatabase(applicationContext)
                .taskDao()
                .getTaskById(taskId)

            setContent {
                Scaffold { padding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(20.dp)
                    ) {

                        Text(
                            text = "Task Details",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        if (task != null) {

                            Text(
                                text = task.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text("Description")
                            Text(task.description)

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text("Deadline")
                            Text(task.deadline)

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text("Status")
                            Text(
                                if (task.isCompleted)
                                    "Completed"
                                else
                                    "Pending"
                            )

                        } else {
                            Text("Task not found.")
                        }

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Button(
                            onClick = { finish() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Tasks")
                        }
                    }
                }
            }
        }
    }
}
