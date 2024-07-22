package com.example.teamwork_management.dataClasses.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.dataClasses.relation.TaskWithUser
import com.example.teamwork_management.dataClasses.relation.TeamCrossRef
import com.example.teamwork_management.dataClasses.relation.TeamWithUser
import com.example.teamwork_management.dataClasses.relation.UserCrossRef

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newUser: List<newUser>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(newTask: newTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(newTeam: newTeam): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCross(crossRef: UserCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamCross(crossRef: TeamCrossRef)
    @Update
    suspend fun update(newUser: newUser)

    @Delete
    suspend fun delete(newUser: newUser)

    @Query("SELECT * FROM newUser WHERE userId = :userId")
    suspend fun getUserById(userId:String): newUser?

    @Transaction
    @Query("SELECT * FROM newTeam WHERE teamId = :teamId")
    suspend fun getUsersOfTeam(teamId: Int): TeamWithUser


    @Transaction
    @Query("SELECT * FROM newTask WHERE taskId = :taskId")
    suspend fun getUsersOfTask(taskId: Int): TaskWithUser

    @Query("SELECT * FROM newUser")
    suspend fun getAllUsers(): List<newUser>
}


