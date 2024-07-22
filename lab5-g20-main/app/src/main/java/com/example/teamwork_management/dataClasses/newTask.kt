package com.example.teamwork_management.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class newTask(

    @PrimaryKey(autoGenerate = true)
    var taskId: Int = 0,

    var title: String = "",
    var description: String? = "",
    var tag: String? = "",
    var category: String? = "",
    var assignedMembers: List<String> = listOf(),
    var dueDate: String? = "",
    var state: String = Status.Open.toString(),
    var comments: List<Int>? = listOf(),
    var histories: List<Int>? = listOf()
)