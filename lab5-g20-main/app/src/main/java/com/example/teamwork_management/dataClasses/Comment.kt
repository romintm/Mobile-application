package com.example.teamwork_management.dataClasses

import java.util.Date

data class Comment(
    val id: Int,
    val taskId: Int,
    private var _author: String,
    private var _date: Date,
    private var _comment: String
) {
    var author: String
        get() = _author
        set(value) {
            _author = value
        }

    var date: Date
        get() = _date
        set(value) {
            _date = value
        }

    var comment: String
        get() = _comment
        set(value) {
            _comment = value
        }
}