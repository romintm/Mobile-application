package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.Task
import com.example.teamwork_management.dataClasses.User
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newUser

data class TaskWithUser(
    @Embedded val newTask: newTask,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "userId",
        associateBy = Junction(UserCrossRef::class)
    )
    val assignedUsers:List<newUser>
)
