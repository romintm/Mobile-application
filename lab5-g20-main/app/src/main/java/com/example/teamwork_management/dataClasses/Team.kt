package com.example.teamwork_management.dataClasses

data class Team(
    val id: Int, //Unique identifier of the team
    private var _name: String,
    private var _img: String,
    private var _description: String,
    private var _category: String,
    private var _creationdate: String,
    var _members: MutableList<User>,
    private var _tasks: MutableList<Task>
) {
    var name: String
        get() = _name
        set(value) {
            _name = value
        }

    var img: String
        get() = _img
        set(value) {
            _img = value
        }

    var description: String
        get() = _description
        set(value) {
            _description = value
        }

    var category: String
        get() = _category
        set(value) {
            _category = value
        }

    var creationdate: String
        get() = _creationdate
        set(value) {
            _creationdate = value
        }

    var members: MutableList<User>
        get() = _members
        set(value) {
            _members = value
        }

    var tasks: MutableList<Task>
        get() = _tasks
        set(value) {
            _tasks = value
        }
}