package com.example.taskmate
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.TaskMateTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.TextButton

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskDao = TaskDatabase.getDatabase(applicationContext).taskDao()

        setContent {
            TaskMateTheme {
                val tasks by taskDao.getAllTasks()
                    .collectAsState(initial = emptyList())

                TaskMateHomeScreen(
                    tasks = tasks,
                    taskDao = taskDao,
                    onBackClick = {
                        finish()
                    },
                    onAddTaskClick = {
                        val intent = Intent(
                            this@MainActivity,
                            AddTaskActivity::class.java
                        )
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

private enum class TaskFilter(val label: String) {
    ALL("All"),
    PENDING("Pending"),
    COMPLETED("Completed")
}

@Composable
fun TaskMateHomeScreen(
    tasks: List<Task>,
    taskDao: TaskDao,
    onBackClick: () -> Unit,
    onAddTaskClick: () -> Unit
) {
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val pendingTasks = totalTasks - completedTasks

    val updateScope = rememberCoroutineScope()
    val context = LocalContext.current
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    var selectedFilter by remember {
        mutableStateOf(TaskFilter.ALL)
    }

    val visibleTasks = when (selectedFilter) {
        TaskFilter.ALL -> tasks
        TaskFilter.PENDING -> tasks.filter { !it.isCompleted }
        TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
    }

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

            Button(onClick = onBackClick) {
                Text(text = "← Back")
            }

            Text(
                text = "TaskMate",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6750A4)
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

            // Task filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilter.values().forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.DarkGray,
                            selectedContainerColor = Color(0xFF42465A),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  Filtered task list
            if (visibleTasks.isEmpty()) {

                val message = when {
                    tasks.isEmpty() ->
                        "No tasks yet. Add your first task!"

                    selectedFilter == TaskFilter.PENDING ->
                        "You have no pending tasks. Great job!"

                    selectedFilter == TaskFilter.COMPLETED ->
                        "No completed tasks yet."

                    else -> "No tasks to show."
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(20.dp),
                        color = Color.Gray
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = visibleTasks,
                        key = { it.id }
                    ) { task ->

                        TaskCard(
                            task = task,
                            onCompletionChanged = { isCompleted ->
                                updateScope.launch {
                                    taskDao.updateTaskStatus(
                                        task.id,
                                        isCompleted
                                    )
                                }
                            },

                            onEditClick = {
                                val intent = Intent(
                                    context,
                                    EditTaskActivity::class.java
                                ).apply {
                                    putExtra("TASK_ID", task.id)
                                }

                                context.startActivity(intent)
                            },
                            onDeleteClick = {
                                taskToDelete = task
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = onAddTaskClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "+ Add Task",
                    fontSize = 16.sp
                )
            }
        }
    }
    if (taskToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                taskToDelete = null
            },
            title = {
                Text("Delete Task")
            },
            text = {
                Text("Are you sure you want to delete this task?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val task = taskToDelete
                        if (task != null) {
                            updateScope.launch {
                                taskDao.deleteTask(task)
                            }
                            Toast.makeText(
                                context,
                                "Task deleted successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        taskToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        taskToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

//  Task completion card
@Composable
fun TaskCard(
    task: Task,
    onCompletionChanged: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
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
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCompletionChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF6750A4),
                    uncheckedColor = Color.DarkGray,
                    checkmarkColor = Color.White
                )
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Deadline: ${task.deadline}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = if (task.isCompleted) "Done" else "Pending",
                color = if (task.isCompleted) {
                    Color(0xFF2E7D32)
                } else {
                    Color(0xFFEF6C00)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onEditClick
            ) {
                Text("Edit")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onDeleteClick
            ) {
                Text("Delete")
            }
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