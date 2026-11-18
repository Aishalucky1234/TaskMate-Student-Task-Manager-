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

class EditTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val titleInput = findViewById<EditText>(R.id.etTaskTitle)
        val descriptionInput = findViewById<EditText>(R.id.etDescription)
        val deadlineInput = findViewById<EditText>(R.id.etDeadline)

        val saveButton = findViewById<Button>(R.id.btnSaveTask)
        val cancelButton = findViewById<Button>(R.id.btnCancel)

        saveButton.text = "Update Task"
        saveButton.isEnabled = false

        val taskId = intent.getIntExtra("TASK_ID", -1)

        if (taskId == -1) {
            Toast.makeText(this, "Invalid task", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val database = TaskDatabase.getDatabase(this)
        val taskDao = database.taskDao()

        var currentTask: Task? = null

        // Load the existing task
        lifecycleScope.launch {
            val task = withContext(Dispatchers.IO) {
                taskDao.getTaskById(taskId)
            }

            if (task == null) {
                Toast.makeText(
                    this@EditTaskActivity,
                    "Task not found",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return@launch
            }

            currentTask = task

            titleInput.setText(task.title)
            descriptionInput.setText(task.description)
            deadlineInput.setText(task.deadline)

            saveButton.isEnabled = true
        }

        // Update the task
        saveButton.setOnClickListener {

            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val deadline = deadlineInput.text.toString().trim()

            if (title.isEmpty()) {
                titleInput.error = "Please enter a task title."
            } else if (deadline.isEmpty()) {
                deadlineInput.error = "Please enter a deadline."
            } else {

                val oldTask = currentTask ?: return@setOnClickListener

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        taskDao.updateTask(
                            oldTask.copy(
                                title = title,
                                description = description,
                                deadline = deadline
                            )
                        )
                    }

                    Toast.makeText(
                        this@EditTaskActivity,
                        "Task updated successfully!",
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
