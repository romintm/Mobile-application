package com.example.teamwork_management.application

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.teamwork_management.dataClasses.db.TeamDatabase
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.models.mainModel
import com.example.teamwork_management.viewModels.mainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class mainApplication: Application() {
    lateinit var model: mainModel

    override fun onCreate() {
        super.onCreate()
    mainApplication.db=Room.databaseBuilder(this.applicationContext,TeamDatabase::class.java,"team_db")
        .fallbackToDestructiveMigration()
        .build()
        val user = listOf(
            newUser(
            userId = "1",
            name = "John",
            surname = "Doe",
            profilePicture = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            email = "john@gmail.com",
            city = "Torino",
            about = "hello I am software engineer",
            userProjects = 2,
            userTasks = 5,
            role = "software manager"
            ),
            newUser(
                userId = "2",
                name = "user2",
                surname = "test2",
                profilePicture = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=1331&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email = "user2@gmail.com",
                city = "Milan",
                about = "Old employee",
                userProjects = 6,
                userTasks = 15,
                role = "Full stack developer"
            ),
            newUser(
                userId = "3",
                name = "Beradly",
                surname = "Kopper",
                profilePicture = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=1331&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email = "user2@gmail.com",
                city = "NewYork",
                about = "Something random",
                userProjects = 2,
                userTasks = 7,
                role = "Sellerman"
            ),
            newUser(
                userId = "4",
                name = "Andrea",
                surname = "Ejani",
                profilePicture = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=1331&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email = "user4@gmail.com",
                city = "Rome",
                about = "Nothing special",
                userProjects = 12,
                userTasks = 45,
                role = "Back-End Developer"
            ),
            newUser(
                userId = "TFmVxCQ2CUZBnFfxGF2fHuQyKSY2",
                name = "Soheil",
                surname = "Jamshidi",
                profilePicture = "https://images.unsplash.com/photo-1520592978680-efbdeae5d036?q=80&w=2787&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email = "soheil.jamshidi@gmail.com",
                city = "Turin",
                about = "I'm web developer",
                userProjects = 1,
                userTasks = 1,
                role = "Admin"
            ),
            )
        val applicationScope = CoroutineScope(Dispatchers.Default)
        applicationScope.launch {
            withContext(Dispatchers.IO) {
                db.userDao().insert(user)
            }
        }
        model = mainModel(this)
    }
    companion object {
        lateinit var db: TeamDatabase
    }
}

class Factory (context: Context): ViewModelProvider.Factory {
    val model = (context.applicationContext as? mainApplication)?.model ?:
    throw IllegalArgumentException("Wrong application class")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(mainViewModel::class.java)){
            return mainViewModel(model) as T
        }
        else throw IllegalArgumentException("Unexpected viewmodel class")
    }
}