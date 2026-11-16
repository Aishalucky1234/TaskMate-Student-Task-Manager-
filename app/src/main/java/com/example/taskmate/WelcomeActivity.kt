package com.example.taskmate



import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.TaskMateTheme

class WelcomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskMateTheme {
                WelcomeScreen {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0F3FF),
                        Color(0xFFF8F5FF),
                        Color.White
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // App name at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✓",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(
                        Color(0xFF5865D8),
                        CircleShape
                    )
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "TaskMate",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3949AB)
            )
        }

        Spacer(modifier = Modifier.height(35.dp))

        // Task checklist illustration
        WelcomeIllustration()

        Spacer(modifier = Modifier.height(35.dp))

        // Welcome message
        Text(
            text = "Your goals,\nbeautifully organized.",
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF303F9F),
            textAlign = TextAlign.Center,
            lineHeight = 39.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Plan your day, manage your deadlines, " +
                    "and make every moment count.",
            fontSize = 16.sp,
            color = Color(0xFF777D96),
            textAlign = TextAlign.Center,
            lineHeight = 25.sp
        )

        Spacer(modifier = Modifier.height(35.dp))

        // Get Started button
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5865D8)
            )
        ) {
            Text(
                text = "Get Started  →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "One task at a time. One step closer.",
            fontSize = 13.sp,
            color = Color(0xFF9299B0)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun WelcomeIllustration() {

    Box(
        modifier = Modifier
            .size(260.dp)
            .background(
                Color(0xFFE2E8FF),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "MY DAILY TASKS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5865D8)
                )

                Spacer(modifier = Modifier.height(16.dp))

                WelcomeTaskRow(
                    task = "Complete assignments",
                    done = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                WelcomeTaskRow(
                    task = "Study for exams",
                    done = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                WelcomeTaskRow(
                    task = "Review lecture notes",
                    done = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .background(
                            Color(0xFFE8EAF6),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .fillMaxHeight()
                            .background(
                                Color(0xFF6574E8),
                                RoundedCornerShape(10.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your progress is looking good!",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun WelcomeTaskRow(
    task: String,
    done: Boolean
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = if (done) "✓" else "○",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (done)
                Color(0xFF27AE80)
            else
                Color(0xFF9AA2B8)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = task,
            fontSize = 12.sp,
            color = Color(0xFF41465C)
        )
    }
}
