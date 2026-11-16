
package com.example.taskmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.TaskMateTheme
import android.content.Intent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskFlow = TaskDatabase.getDatabase(applicationContext)
            .taskDao()
            .getAllTasks()

        setContent {
            val tasks by taskFlow.collectAsState(initial = emptyList())

            TaskMateHomeScreen(
                tasks = tasks,
                onBackClick = {
                    finish()
                },
                onAddTaskClick = {
                    val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
                    this@MainActivity.startActivity(intent)
                }
            )
        }
    }
}
@Composable
fun TaskMateHomeScreen(
    tasks: List<Task>,
    onBackClick: () -> Unit,
    onAddTaskClick: () -> Unit
) {
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val pendingTasks = totalTasks - completedTasks
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF5F6FA)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Button(
                onClick = onBackClick
            ) {
                Text(text = "← Back")
            }
            Text(
                text = "TaskMate",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(color =(0xFF6750A4))
            )

            Text(
                text = "Organize your student life",
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Good morning! 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6750A4)
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ready to make today productive?",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier.height(6.dp))

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    title = "Total",
                    count = totalTasks.toString(),
                    color = Color(0xFF3949AB),
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Pending",
                    count = pendingTasks.toString(),
                    color = Color(0xFFEF6C00),
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Done",
                    count = completedTasks.toString(),
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "My Tasks",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (tasks.isEmpty()) {
                Text(
                    text = "No tasks yet. Add your first task!",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                tasks.forEach { task ->
                    TaskCard(
                        title = task.title,
                        deadline = "Deadline: ${task.deadline}",
                        completed = task.isCompleted
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddTaskClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "+ Add Task", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun TaskCard(
    title: String,
    deadline: String,
    completed: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = deadline,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = if (completed) "Done" else "Pending",
                color = if (completed) Color(0xFF2E7D32)
                else Color(0xFFEF6C00),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun SummaryCard(
    title: String,
    count: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}