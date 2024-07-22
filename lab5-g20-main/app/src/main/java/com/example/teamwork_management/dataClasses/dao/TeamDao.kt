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
import com.example.teamwork_management.dataClasses.relation.TaskWithTeam
import com.example.teamwork_management.dataClasses.relation.TeamCrossRef
import com.example.teamwork_management.dataClasses.relation.UserCrossRef
import com.example.teamwork_management.dataClasses.relation.UserWithTeam

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTeam(newTeam: newTeam)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(newUser: newUser)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(newTask: newTask)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamCross(crossRef: TeamCrossRef)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCross(crossRef: UserCrossRef)

    @Update
    suspend fun update(newTeam: newTeam)

    @Delete
    suspend fun delete(newTeam: newTeam)

    @Query("SELECT * FROM newTeam WHERE teamId = :teamId")
    suspend fun getTeamById(teamId: Int): newTeam?

    @Query("SELECT * FROM newTeam")
    suspend fun getAllTeams(): List<newTeam>

    @Transaction
    @Query("SELECT * FROM newUser WHERE userId = :userId")
    suspend fun getTeamsOfUser(userId: String): UserWithTeam

    @Transaction
    @Query("SELECT * FROM newTask WHERE taskId = :taskId")
    suspend fun getTeamsOfTask(taskId: Int): TaskWithTeam


}