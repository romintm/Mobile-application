package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.newComment
import com.example.teamwork_management.dataClasses.newTask

data class TaskWithComment (
    @Embedded val newTask: newTask,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val newComment: List<newComment>
)