package com.example.teamwork_management.dataClasses.relation

import androidx.room.Entity


@Entity(primaryKeys = ["teamId" , "userId"])
data class TeamCrossRef(
    val teamId:Int,
    val userId:String
)
