package com.example.teamwork_management.dataClasses.relation

import androidx.room.Entity
@Entity(primaryKeys = [ "userId" ,"taskId"])
class UserCrossRef (

    val userId:String,
    val taskId:Int
)
