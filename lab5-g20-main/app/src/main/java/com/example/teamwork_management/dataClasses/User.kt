package com.example.teamwork_management.dataClasses

data class User(
    val id: String = "", //Unique identifier for the user
    var _name: String = "",
    var _surname: String = "",
    var _profilePicture: String? = "",
    var _email: String = "",
    var _city: String = "",
    var _about: String = "",
    var _userTeams: Int = 0,
    var _userProjects: Int = 0,
    var _userTasks: Int = 0,
    var _role: String? = ""
) {
    var name: String
        get() = _name
        set(value) {
            _name = value
        }

    var surname: String
        get() = _surname
        set(value) {
            _surname = value
        }

    var profilePicture: String
        get() = _profilePicture!!
        set(value) {
            _profilePicture = value
        }

    var email: String
        get() = _email!!
        set(value) {
            _email = value
        }

    var city: String
        get() = _city!!
        set(value) {
            _city = value
        }

    var about: String
        get() = _about!!
        set(value) {
            _about = value
        }

    var userTeams: Int
        get() = _userTeams!!
        set(value) {
            _userTeams = value
        }

    var userProjects: Int
        get() = _userProjects!!
        set(value) {
            _userProjects = value
        }

    var userTasks: Int
        get() = _userTasks!!
        set(value) {
            _userTasks = value
        }

    var role: String
        get() = _role!!
        set(value) {
            _role = value
        }
}