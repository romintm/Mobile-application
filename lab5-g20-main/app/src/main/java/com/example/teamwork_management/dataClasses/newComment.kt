package com.example.teamwork_management.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class newComment(

    @PrimaryKey(autoGenerate = true)
    var commentId: Int = 0,
    val taskId:Int = 0,
    var author: String = "",
    var date: String = "",
    var comment: String = ""
)
