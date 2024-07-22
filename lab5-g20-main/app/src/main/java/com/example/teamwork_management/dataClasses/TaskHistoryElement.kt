package com.example.teamwork_management.dataClasses

import java.util.Date

data class TaskHistoryElement(
    val id: Int, // Unique identifier for the task history element
    val taskId: Int,
    private var _author: String,
    private var _action: String,
    private var _date: Date
) {
    var author: String
        get() = _author
        set(value) {
            _author = value
        }

    var action: String
        get() = _action
        set(value) {
            _action = value
        }

    var date: Date
        get() = _date
        set(value) {
            _date = value
        }

    fun print() : String {
        return date.toString() + ": " + author + action + "\n"
    }
}