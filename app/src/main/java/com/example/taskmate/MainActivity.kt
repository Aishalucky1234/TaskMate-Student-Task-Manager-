package com.example.taskmate

import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmate.ui.theme.TaskMateTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.TextStyle

data class Task(
    val title: String,
    val description: String,
    val deadline: String,
    val completed: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskMateTheme {
                TaskMateApp()
            }
        }
    }
}

@Composable
fun TaskMateApp() {
    val navController = rememberNavController()

    val tasks = remember {
        mutableStateListOf(
            Task("Complete Mobile Computing project", "Work on the project", "Friday"),
            Task("Study for the quiz", "Review the lectures", "Sunday", true),
            Task("Review lecture notes", "Prepare for class", "Monday")
        )
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            TaskMateHomeScreen(tasks = tasks) {
                navController.navigate("addTask")
            }
        }

        composable("addTask") {
            AddTaskScreen(
                onBack = { navController.popBackStack() },
                onSave = { title, description, deadline ->
                    tasks.add(Task(title, description, deadline))
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun TaskMateHomeScreen(tasks: List<Task>, onAddTask: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF5F6FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "TaskMate",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3949AB)
            )
            Text("Organize your student life", fontSize = 15.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))
            Text("My Tasks", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))

            if (tasks.isEmpty()) {
                Text("No tasks yet. Add your first task!")
            }

            tasks.forEach { task ->
                TaskCard(task)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onAddTask, modifier = Modifier.fillMaxWidth()) {
                Text("+ Add Task", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(task.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Deadline: ${task.deadline}", fontSize = 13.sp, color = Color.Gray)
            }

            Text(
                text = if (task.completed) "Done" else "Pending",
                color = if (task.completed) Color(0xFF2E7D32) else Color(0xFFEF6C00),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AddTaskScreen(
    onBack: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(containerColor = Color(0xFFF5F6FA)) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Add Task", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF3949AB))
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task title") },
                placeholder = { Text("e.g. Study for the quiz") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(color = Color.DarkGray),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color(0xFF3949AB),
                    unfocusedLabelColor = Color.Gray,
                    focusedBorderColor = Color(0xFF3949AB),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF3949AB)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("Enter task details") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                textStyle = TextStyle(color = Color.DarkGray),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color(0xFF3949AB),
                    unfocusedLabelColor = Color.Gray,
                    focusedBorderColor = Color(0xFF3949AB),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF3949AB)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Deadline") },
                placeholder = { Text("e.g. 30/10/2026") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(color = Color.DarkGray),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color(0xFF3949AB),
                    unfocusedLabelColor = Color.Gray,
                    focusedBorderColor = Color(0xFF3949AB),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF3949AB)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Enter the deadline for your task.", fontSize = 12.sp, color = Color.Gray)

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = Color(0xFFB3261E))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (title.isBlank()) {
                        errorMessage = "Please enter a task title."
                    } else if (deadline.isBlank()) {
                        errorMessage = "Please enter a deadline."
                    } else {
                        onSave(title.trim(), description.trim(), deadline.trim())
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }
    }
}