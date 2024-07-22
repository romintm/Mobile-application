package com.example.teamwork_management.dataClasses.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.newComment
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.dataClasses.relation.TaskCrossRef
import com.example.teamwork_management.dataClasses.relation.TaskWithComment
import com.example.teamwork_management.dataClasses.relation.TaskWithHistory
import com.example.teamwork_management.dataClasses.relation.TeamCrossRef
import com.example.teamwork_management.dataClasses.relation.TeamWithTask
import com.example.teamwork_management.dataClasses.relation.UserCrossRef
import com.example.teamwork_management.dataClasses.relation.UserWithTask


@Dao
interface TaskDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(newTask: newTask): Long

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertComment(newComment: newComment)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertHistory (newHistory: NewHistory)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTeam (newTeam: newTeam)
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTeamCross (crossRef: TeamCrossRef)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertUser (newUser: newUser)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertUserCross (crossRef: UserCrossRef)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTaskCross (crossRef: TaskCrossRef)

        @Update
        suspend fun update(newTask: newTask)

        @Delete
        suspend fun delete(newTask: newTask)

        @Query("SELECT * FROM newTask WHERE taskId = :taskId")
        suspend fun getTaskById (taskId: Int): newTask?

        @Transaction
        @Query("SELECT * FROM newTask WHERE taskId = :taskId")
        suspend fun getTaskAndCommentWithId(taskId: Int):List<TaskWithComment>

        @Transaction
        @Query("SELECT * FROM newTask WHERE taskId = :taskId")
        suspend fun getTaskAndHistoryWithId(taskId: Int):List<TaskWithHistory>

        @Transaction
        @Query("SELECT * FROM newTeam WHERE teamId = :teamId")
        suspend fun getTaskOfTheTeam(teamId:Int):TeamWithTask

        @Transaction
        @Query("SELECT * FROM newUser WHERE userId = :userId")
        suspend fun getTaskOfTheUser(userId:String):UserWithTask
}
