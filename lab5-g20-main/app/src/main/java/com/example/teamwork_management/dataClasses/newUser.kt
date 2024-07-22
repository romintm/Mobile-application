package com.example.teamwork_management.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class newUser(

    @PrimaryKey
    val userId: String = "",


    var name: String = "",
    var surname: String = "",
    var profilePicture: String? = "",
    var email: String = "",
    var city: String = "",
    var about: String = "",
    var userProjects: Int = 0,
    var userTasks: Int = 0,
    var role: String? = ""
)