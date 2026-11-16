package com.example.taskmate


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val titleInput = findViewById<EditText>(R.id.etTaskTitle)
        val descriptionInput = findViewById<EditText>(R.id.etDescription)
        val deadlineInput = findViewById<EditText>(R.id.etDeadline)

        val saveButton = findViewById<Button>(R.id.btnSaveTask)
        val cancelButton = findViewById<Button>(R.id.btnCancel)

        saveButton.setOnClickListener {

            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val deadline = deadlineInput.text.toString().trim()

            if (title.isEmpty()) {
                titleInput.error = "Please enter a task title."
            } else if (deadline.isEmpty()) {
                deadlineInput.error = "Please enter a deadline."
            } else {

                val database = TaskDatabase.getDatabase(this)

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        database.taskDao().insertTask(
                            Task(
                                title = title,
                                description = description,
                                deadline = deadline
                            )
                        )
                    }

                    Toast.makeText(
                        this@AddTaskActivity,
                        "Task saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
}