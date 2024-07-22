package com.example.teamwork_management.dataClasses.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser

data class UserWithTeam(
    @Embedded val newUser: newUser,
    @Relation(
    parentColumn = "userId",
    entityColumn = "teamId",
    associateBy = Junction(TeamCrossRef::class)
)
    val teams: List<newTeam>
)
