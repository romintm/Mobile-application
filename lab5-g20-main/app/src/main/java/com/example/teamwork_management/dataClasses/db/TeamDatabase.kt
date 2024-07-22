package com.example.teamwork_management.dataClasses.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.dao.CommentDao
import com.example.teamwork_management.dataClasses.dao.HistoryDao
import com.example.teamwork_management.dataClasses.dao.TaskDao
import com.example.teamwork_management.dataClasses.dao.TeamDao
import com.example.teamwork_management.dataClasses.dao.UserDao
import com.example.teamwork_management.dataClasses.newComment
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.dataClasses.relation.TaskCrossRef
import com.example.teamwork_management.dataClasses.relation.TeamCrossRef
import com.example.teamwork_management.dataClasses.relation.UserCrossRef


@Database(entities = [newTask::class, newTeam::class, newUser::class, NewHistory::class,newComment::class,UserCrossRef::class,TeamCrossRef::class,TaskCrossRef::class] , version = 4)
@TypeConverters(Converters::class)
abstract class TeamDatabase:RoomDatabase() {
    abstract fun commentDao():CommentDao
    abstract fun historyDao():HistoryDao
    abstract fun taskDao():TaskDao
    abstract fun teamDao():TeamDao
    abstract fun userDao():UserDao
}
