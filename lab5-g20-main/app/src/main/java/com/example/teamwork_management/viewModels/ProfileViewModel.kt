package com.example.teamwork_management.viewModels

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {
    // other info if needed

    private val _isPhotoMode = MutableLiveData<Boolean>()
    val isPhotoMode: LiveData<Boolean> = _isPhotoMode

    init {
        // Initialize the edit mode to false by default
        _isPhotoMode.value = true
    }

    fun togglePhotoMode () {
        _isPhotoMode.value = !_isPhotoMode.value!!
    }

        //state to check the edit mode
    private val _isEditMode = MutableLiveData<Boolean>()
    val isEditMode: LiveData<Boolean> = _isEditMode

    init {
        // Initialize the edit mode to false by default
        _isEditMode.value = false
    }

    fun validateFields(): Boolean {
        checkName()
        checkNickName()
        checkEmail()
        checkUserLocation()
        checkDescription()
        if (fullNameError.isNotBlank() || nicknameUserError.isNotBlank() || userEmailError.isNotBlank() || userLocationError.isNotBlank() || descriptionsError.isNotBlank()) {
            return false
        } else return true
    }

    // Expose method to toggle edit mode
    fun toggleEditMode() {
        var validField = validateFields()
        if (validField) {
            _isEditMode.value = !_isEditMode.value!!
        }
    }

    //Full name
    var fullName by mutableStateOf("Michele Slave")
        private set
    var fullNameError by mutableStateOf("")
        private set

    fun setName(n: String) {
        fullName = n
    }

    private fun checkName() {
        if (fullName.isBlank()) {
            fullNameError = "The name cannot be blank"
        } else fullNameError = ""
    }


    //UserName
    var nicknameuser by mutableStateOf("MicheleS")
        private set
    var nicknameUserError by mutableStateOf("")
        private set

    fun setNickName(ni: String) {
        nicknameuser = ni
    }

    private fun checkNickName() {
        if (nicknameuser.isBlank()) {
            nicknameUserError = "The nickName cannot be blank"
        } else nicknameUserError = ""
    }

    // Email
    var userEmail by mutableStateOf("Michele@gmail.com")
        private set
    var userEmailError by mutableStateOf("")
        private set

    fun setEmail(e: String) {
        userEmail = e
    }

    private fun checkEmail() {
        if (userEmail.isBlank()) {
            userEmailError = "The email cannot be blank"
        } else if (!userEmail.contains('@')) {
            userEmailError = "The email has invalid format"
        } else userEmailError = ""
    }

    // Location
    var userLocation by mutableStateOf("Turin")
        private set
    var userLocationError by mutableStateOf("")
        private set

    fun setLocation(l: String) {
        userLocation = l
    }

    private fun checkUserLocation() {
        if (userLocation.isBlank()) {
            userLocationError = "The user location cannot be blank"
        } else userLocationError = ""
    }

    //Description
    var userDescriptions by mutableStateOf("I worked as a Frontend developer")
        private set
    var descriptionsError by mutableStateOf("")
        private set

    fun setDescriptions(d: String) {
        userDescriptions = d
    }

    fun checkDescription() {
        if (userDescriptions.isBlank()) {
            descriptionsError = "The description cannot be blank"
        } else descriptionsError = ""
    }

    //# of Teams
    var userTeams by mutableStateOf("3")
        private set
    var userTeamsError by mutableStateOf("")
        private set

    fun setuserTeams(d: String) {
        userTeams = d
    }

    //# of Projects
    var userProjects by mutableStateOf("8")
        private set
    var userProjectsError by mutableStateOf("")
        private set

    fun setuserProjects(d: String) {
        userProjects = d
    }


    //# of Tasks
    var userTasks by mutableStateOf("37")
        private set
    var userTasksError by mutableStateOf("")
        private set

    fun setuserTasks(d: String) {
        userTasks = d
    }

    //Profile Picture

    var userProfilePicture by mutableStateOf("https://images.unsplash.com/photo-1551438632-e8c7d9a5d1b7?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
    fun setProfilePicture(s : String){
        userProfilePicture = s
    }
    var picTaken: Bitmap? = null
        private set
    fun setPicTaken(n: Bitmap?) {
        picTaken = n
    }

    var userPPError by mutableStateOf("")
        private set

    fun setpp(d: String) {
        userPPError = d
    }


    fun getMonogram(): String {
        val names = fullName.split(" ")
        return if (names.size >= 2 && names[0].first() != null && names[1].first() !== null) {
            "${names[0].first()}${names[1].first()}"
        } else {
            ""
        }
    }

    // here add logic of picture
    fun hasPicture(): Int {
        if (userProfilePicture.isEmpty() && picTaken == null) {
            return 0
        } else if (userProfilePicture.isNotEmpty() && picTaken == null) {
            return 1
        } else if (userProfilePicture.isEmpty() && picTaken != null) {
            return 2
        } else {
            return 3
        }
    }
}
