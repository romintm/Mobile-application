package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newTeam

data class TeamWithTask (
    @Embedded val newTeam: newTeam,
    @Relation(
        parentColumn = "teamId",
        entityColumn = "taskId" ,
        associateBy = Junction(TaskCrossRef::class)
    )
    val tasks: List<newTask>
)
