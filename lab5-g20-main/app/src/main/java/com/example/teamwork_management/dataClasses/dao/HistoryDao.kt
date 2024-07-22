package com.example.teamwork_management.dataClasses.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.relation.TaskWithHistory

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newHistory: NewHistory): Long

    @Update
    suspend fun update(newHistory: NewHistory)

    @Delete
    suspend fun delete(newHistory: NewHistory)

    @Query("SELECT * FROM newhistory WHERE historyId = :historyId")
    suspend fun getTaskHistoryElementById(historyId: Int): NewHistory

    @Transaction
    @Query("SELECT * FROM newTask WHERE taskId = :taskId")
    suspend fun getHistoriesByTaskId(taskId: Int): TaskWithHistory
}
