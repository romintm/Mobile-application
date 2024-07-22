package com.example.teamwork_management.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teamwork_management.dataClasses.Comment
import com.example.teamwork_management.dataClasses.Status
import com.example.teamwork_management.dataClasses.Task
import com.example.teamwork_management.dataClasses.newTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class TasksViewModel: ViewModel() {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    //list of Tasks
    private var _Tasks: MutableStateFlow<MutableSet<Task>> =  MutableStateFlow(mutableSetOf(
        Task(
            id = 1,
            _title = "Add new filter features",
            _description = "Description for Task 1",
            _tag = "Important",
            _category = "Work",
            _assignedMembers = mutableListOf ("Payne Junon", "Gardenia Jonny", "Jinan Iudris"),
            _dueDate = dateFormat.parse("2024-05-10"),
            _state = "Pending",
            _comments = mutableListOf (),
            _history= mutableListOf()
        ),
        Task(
            id = 2,
            _title = "Add validation for creating new task",
            _description = "Description for Task 2",
            _tag = "Important",
            _category = "Work",
            _assignedMembers = mutableListOf ("Deodato Debbie"),
            _dueDate = dateFormat.parse("2024-05-10"),
            _state = "Pending",
            _comments = mutableListOf (),
            _history= mutableListOf()

        ),
    ))
    var Tasks: Set<Task> = _Tasks.value

    fun addNewTask(task: Task) {
        val tasks = _Tasks.value
        tasks.add(task)
        _Tasks.value = tasks
        println(_Tasks.value)
    }
    //Create a new Task

    var id:Int = 0

    private var title: String = ""

    var _titleError = MutableLiveData("")
    val titleError: LiveData<String> = _titleError

    fun setTitleError (){
        _titleError.value = "Title Can not be empty"
    }

    fun getTitle(): String {
        return title
    }
    fun setTitle(value: String) {
        title += value
    }

    var description: String = ""

    var tag: String = ""

    var category: String = ""

    var members: MutableList<String> = mutableListOf()

    private var _membersError = MutableLiveData("")
    val membersError = _membersError.value

    fun setMembersError (){
        _membersError.value = "Title Can not be empty"
    }

    var dueDate: Date? = null

    var status: Status = Status.Open

    fun newTask(title: String, description: String, tag: String = "", category: String, members: MutableList<String>, dueDate: String, status: String) {
        var id: Int? = null

        if (title.isBlank() || members.isEmpty()){

            if (title.isBlank()) _titleError.value = "Name of the Task can not be empty."
            if (members.isEmpty()) setMembersError()

        } else {
            if (_Tasks.value.isNotEmpty()){
                id = _Tasks.value.last().id + 1
            } else {
                id = 0
            }

            var newTask = Task(id, title, description, tag, category, members, dateFormat.parse(dueDate), status, mutableListOf(), mutableListOf())

            addNewTask(newTask)
            toggleCreateNewTask()
        }
    }

    //State to open the CreateNewTask page
    private var _isCreateNewTask = MutableLiveData(false)
    val createNewTask: LiveData<Boolean> = _isCreateNewTask

    fun toggleCreateNewTask () {
        _isCreateNewTask.value = !_isCreateNewTask.value!!
    }

    //State to check the edit mode
    private val _isEditMode = MutableLiveData(false)
    val isEditMode: LiveData<Boolean> = _isEditMode

    //TODO: adding validation for edit screen
    fun toggleEditMode() {
        _isEditMode.value = !_isEditMode.value!!
    }

    //Title of the NavBar
    private var _pageTitle = MutableLiveData("Task List")
    val pageTitle = _pageTitle.value

    private val _activeTask = MutableLiveData<newTask?>(null)
    val activeTask: LiveData<newTask?> = _activeTask

    fun setActiveTask(task: newTask?) {
        _activeTask.value = task
    }

    // Methods to update active task details

    fun updateActiveTaskTitle(title: String) {
        val currentTask = _activeTask.value ?: return
        currentTask.title = title
        _activeTask.value = currentTask
    }

    fun updateActiveTaskDescription(description: String) {
        val currentTask = _activeTask.value ?: return
        currentTask.description = description
        _activeTask.value = currentTask
    }

    fun updateActiveTaskTag(tag: String) {
        val currentTask = _activeTask.value ?: return
        currentTask.tag = tag
        _activeTask.value = currentTask
    }

    fun updateActiveTaskCategory(category: String) {
        val currentTask = _activeTask.value ?: return
        currentTask.category = category
        _activeTask.value = currentTask
    }

    fun updateActiveTaskAssignMembers(members: MutableList<String>?) {
        var memberAssign = _activeTask.value ?: return
        memberAssign.assignedMembers = members ?: mutableListOf()
        _activeTask.value = memberAssign
    }

    fun updateActiveTaskDueDate(dueDate: String) {
        val currentTask = _activeTask.value ?: return
        currentTask.dueDate = dueDate
        _activeTask.value = currentTask
    }

    fun updateActiveTaskState(state: String) {
        val currentTask = _activeTask.value ?: return
        currentTask.state = state
        _activeTask.value = currentTask
    }
    // Method to add a new comment to the active task
    /*fun addComment(comment: Comment) {
        val currentTask = _activeTask.value ?: return
        currentTask.comments!!.add(comment)
        _activeTask.value = currentTask
    }*/

}