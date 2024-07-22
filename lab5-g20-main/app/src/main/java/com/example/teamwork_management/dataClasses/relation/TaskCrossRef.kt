package com.example.teamwork_management.dataClasses.relation

import androidx.room.Entity


@Entity(primaryKeys = ["taskId" , "teamId"])
data class TaskCrossRef(
    val taskId:Int,
    val teamId:Int
)
