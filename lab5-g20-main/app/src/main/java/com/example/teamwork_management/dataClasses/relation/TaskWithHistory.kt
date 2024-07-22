package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.newTask


data class TaskWithHistory (
    @Embedded val newTask: newTask,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val newHistory: List<NewHistory>
 )