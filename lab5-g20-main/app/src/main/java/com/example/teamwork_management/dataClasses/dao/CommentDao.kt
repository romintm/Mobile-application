package com.example.teamwork_management.dataClasses.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.teamwork_management.dataClasses.newComment
import com.example.teamwork_management.dataClasses.relation.TaskWithComment


@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newComment: newComment): Long

    @Update
    suspend fun update(newComment: newComment)

    @Delete
    suspend fun delete(newComment: newComment)

    @Query("SELECT * FROM newComment WHERE commentId = :commentId")
    suspend fun getTaskHistoryElementById(commentId: Int): newComment
     @Transaction
    @Query("SELECT * FROM newTask WHERE taskId = :taskId")
    suspend fun getCmByTaskId(taskId: Int): TaskWithComment
}