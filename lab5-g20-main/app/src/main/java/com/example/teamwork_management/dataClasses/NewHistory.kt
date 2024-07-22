package com.example.teamwork_management.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date



@Entity
data class NewHistory (
    @PrimaryKey(autoGenerate = true)
    var historyId: Int? = 0, // Unique identifier for the task history element
    var taskId: Int? = 0,
    var author: String = "",
    var action: String = "",
    var date: String = ""
)
