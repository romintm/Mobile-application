package com.example.teamwork_management.dataClasses
import java.util.Date

data class Task(
    val id: Int, // Unique identifier for the task
    private var _title: String,
    private var _description: String?,
    private var _tag: String?,
    private var _category: String?,
    private var _assignedMembers: MutableList<String>,
    private var _dueDate: Date?,
    private var _state: String,
    private var _comments: MutableList<Comment>?,
    private var _history: MutableList<TaskHistoryElement>?
) {

    var title: String
        get() = _title
        set(value) {
            _title = value
        }

    var description: String
        get() = _description!!
        set(value) {
            _description = value
        }

    var tag: String
        get() = _tag!!
        set(value) {
            _tag = value
        }

    var category: String
        get() = _category!!
        set(value) {
            _category = value
        }

    var assignedMembers: MutableList<String>
        get() = _assignedMembers
        set(value) {
            _assignedMembers = value
        }

    var dueDate: Date
        get() = _dueDate!!
        set(value) {
            _dueDate = value
        }

    var state: String
        get() = _state
        set(value) {
            _state = value
        }

    var comments: MutableList<Comment>
        get() = _comments!!
        set(value) {
            _comments = value
        }

    var history: MutableList<TaskHistoryElement>
        get() = _history!!
        set(value) {
            _history = value
        }
}