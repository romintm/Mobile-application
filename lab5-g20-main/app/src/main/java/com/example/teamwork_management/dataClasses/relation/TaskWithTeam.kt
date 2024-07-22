package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newTeam

data class TaskWithTeam(
    @Embedded val newTask: newTask,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "teamId" ,
        associateBy = Junction(TaskCrossRef::class)
    )
    val team: List<newTeam>
)
