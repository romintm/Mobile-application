package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser


data class TeamWithUser(
    @Embedded val newTeam: newTeam,
    @Relation(
        parentColumn = "teamId",
        entityColumn = "userId",
        associateBy = Junction(TeamCrossRef::class)
    )
    val users: List<newUser>
)
