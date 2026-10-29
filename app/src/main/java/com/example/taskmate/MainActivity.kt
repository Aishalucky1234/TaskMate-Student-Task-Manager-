
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskMateTheme {
                TaskMateHomeScreen()
            }
        }
    }
}

@Composable
fun TaskMateHomeScreen() {
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
            Text(
                text = "TaskMate",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3949AB)
            )

            Text(
                text = "Organize your student life",
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "My Tasks",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            TaskCard(
                title = "Complete Mobile Computing project",
                deadline = "Deadline: Friday",
                completed = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            TaskCard(
                title = "Study for the quiz",
                deadline = "Deadline: Sunday",
                completed = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            TaskCard(
                title = "Review lecture notes",
                deadline = "Deadline: Monday",
                completed = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "+ Add Task",
                    fontSize = 16.sp
                )
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
