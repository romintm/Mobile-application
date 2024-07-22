package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.newTask

import com.example.teamwork_management.dataClasses.newUser

data class UserWithTask(
    @Embedded val newUser: newUser,
    @Relation (
        parentColumn = "userId",
        entityColumn = "taskId",
        associateBy = Junction(UserCrossRef::class)
    )
    val tasksCount:List<newTask>,
)
