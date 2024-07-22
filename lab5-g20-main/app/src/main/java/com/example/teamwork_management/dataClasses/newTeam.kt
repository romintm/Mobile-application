package com.example.teamwork_management.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class newTeam(

    @PrimaryKey(autoGenerate = true)
    var teamId: Int = 0, //Unique identifier of the team

    var name: String = "",
    var img: String = "",
    var description: String = "",
    var category: String = "",
    var creationdate: String = "",
)
