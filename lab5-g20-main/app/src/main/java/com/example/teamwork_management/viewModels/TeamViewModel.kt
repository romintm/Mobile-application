package com.example.teamwork_management.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamwork_management.dataClasses.Task
import com.example.teamwork_management.dataClasses.Team
import com.example.teamwork_management.dataClasses.User
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.models.mainModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class TeamViewModel: ViewModel() {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    var initialTeam : MutableStateFlow<List<Team>> = MutableStateFlow(listOf())

    // Initialize MutableLiveData with the initial Team object
    private var _Teams = initialTeam
    var Teams: List<Team> = _Teams.value

    fun removeUser (user: User) {
        val newTeams = _Teams.value
        newTeams[0].members.remove(user)
        _Teams.value = newTeams
    }

    private val _selectedUser = MutableLiveData<User?>(null)
    var selectedUser = _selectedUser
    fun setUser (user: User) {
        _selectedUser.value = user
    }


    private val _selectedTeam = MutableLiveData<newTeam>(null)
    var selectedTeam = _selectedTeam

    fun setTeam (team: newTeam) {
        _selectedTeam.value = team
    }

    private val _achievementTeam = MutableLiveData<Team?>(null)
    var achievementTeam: LiveData<Team?> = _achievementTeam

    fun setAchievementTeam (team: Team) {
        _achievementTeam.value = team
    }



    private val _titleError = MutableStateFlow("")
    val titleError = _titleError.asStateFlow()

    private val _descriptionError = MutableStateFlow("")
    val descriptionError = _descriptionError.asStateFlow()

    private val _categoryError = MutableStateFlow("")
    val categoryError = _categoryError.asStateFlow()

    fun newTeam(
        title: String,
        img: String,
        description: String,
        category: String,
        creationdate: String,
        members: MutableList<User>,
        tasks: MutableList<Task>
    ) {
        if (title.isBlank() || description.isBlank() || category.isBlank()) {
            if (title.isBlank()) _titleError.value = "Name of the Team cannot be empty."
            if (description.isBlank()) _descriptionError.value = "Description of the Team cannot be empty."
            if (category.isBlank()) _categoryError.value = "Category of the Team cannot be empty."
        } else {
            val id = if (_Teams.value.isNotEmpty()) {
                _Teams.value.maxOf { it.id } + 1
            } else {
                0
            }

            val newTeam = Team(id, title, img, description, category, creationdate, members, tasks)
            addNewTeam(newTeam)
        }
    }

    fun addNewTeam(newTeam: Team) {
        val updatedTeams = _Teams.value.toMutableList().apply { add(newTeam) }
        _Teams.value = updatedTeams
        println(_Teams.value)
    }
//    fun updateTeam(
//        teamId: Int,
//        title: String,
//        img: String,
//        description: String,
//        category: String,
//        creationdate: String,
//        members: MutableList<User>,
//        tasks: MutableList<Task>
//    ) {
//        if (title.isBlank() || description.isBlank() || category.isBlank()) {
//            if (title.isBlank()) _titleError.value = "Name of the Team cannot be empty."
//            if (description.isBlank()) _descriptionError.value = "Description of the Team cannot be empty."
//            if (category.isBlank()) _categoryError.value = "Category of the Team cannot be empty."
//        } else {
//            val existingTeamIndex = initialTeam.value.indexOfFirst { it.id == teamId }
//            if (existingTeamIndex != -1) {
//                val updatedTeam = initialTeam.value[existingTeamIndex].copy(
//                    _name = title,
//                    _img = img,
//                    _description = description,
//                    _category = category,
//                    _creationdate = creationdate,
//                    _members = members.toMutableList(),
//                    _tasks = tasks.toMutableList()
//                )
//                val updatedTeams = initialTeam.value.toMutableList()
//                updatedTeams[existingTeamIndex] = updatedTeam
//                initialTeam.value = updatedTeams
//            } else {
//                println("Team with ID $teamId not found.")
//            }
//        }
//    }
//
//    fun updateNameTeam(name: String) {
//        val currentTeam = _selectedTeam.value ?: return
//        val updatedTeam = currentTeam.copy(name = name)
//        _selectedTeam.value = updatedTeam
//        val updatedTeams = initialTeam.value.map { if (it.id == updatedTeam.teamId) updatedTeam else it }
//        initialTeam.value = updatedTeams
//    }
//
//    fun updateDescriptionTeam(description: String) {
//        val currentTeam = _selectedTeam.value ?: return
//        val updatedTeam = currentTeam.copy(description = description)
//        _selectedTeam.value = updatedTeam
//        val updatedTeams = initialTeam.value.map { if (it.id == updatedTeam.teamId) updatedTeam else it }
//        initialTeam.value = updatedTeams
//    }
//
//    fun updateCategoryTeam(category: String) {
//        val currentTeam = _selectedTeam.value ?: return
//        val updatedTeam = currentTeam.copy(category = category)
//        _selectedTeam.value = updatedTeam
//        val updatedTeams = initialTeam.value.map { if (it.id == updatedTeam.teamId) updatedTeam else it }
//        initialTeam.value = updatedTeams
//    }
//    fun updateImgTeam(img: String) {
//        val currentTeam = _selectedTeam.value ?: return
//        val updatedTeam = currentTeam.copy(img = img)
//        _selectedTeam.value = updatedTeam
//        val updatedTeams = initialTeam.value.map { if (it.id == updatedTeam.teamId) updatedTeam else it }
//        initialTeam.value = updatedTeams
//    }


}