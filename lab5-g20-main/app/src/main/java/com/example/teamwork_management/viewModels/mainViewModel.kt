package com.example.teamwork_management.viewModels


import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamwork_management.dataClasses.NavbarInfo
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.User
import com.example.teamwork_management.dataClasses.newComment
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.models.mainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class mainViewModel(val model: mainModel): ViewModel() {

    private var _rightButton: MutableState<NavbarInfo> = mutableStateOf(NavbarInfo("", null, "") {})
    val rightButton = _rightButton

    fun setRightButton (title: String,icon: ImageVector?, content: String, bfun: () -> Unit) {
        _rightButton.value = NavbarInfo(title, icon, content, bfun)
    }

    private var _leftButton: MutableState<NavbarInfo> = mutableStateOf(NavbarInfo("Home", Icons.Default.Home, "Home") {})
    val leftButton = _leftButton

    fun setLeftButton (title: String, icon: ImageVector?, content: String, bfun: () -> Unit) {
        _leftButton.value = NavbarInfo(title, icon, content, bfun)
        val stackTrace = Thread.currentThread().stackTrace
        Log.e("TEAM-BA", "Title: ${title}: Full stack trace:")
        for (frame in stackTrace) {
            Log.e("TEAM-BA", "${frame.className}.${frame.methodName}:${frame.lineNumber}\n\n")
        }
    }

    private var _navbarTitle = MutableStateFlow("Teams")
    val navbarTitle = _navbarTitle
    fun setTitle (title: String) {
        _navbarTitle.value = title
//        Log.e("TEAM-NAME", "The title is: ${title}")
//        val stackTrace = Thread.currentThread().stackTrace
//        Log.e("TEAM-NAME", "Full stack trace:")
//        for (frame in stackTrace) {
//            Log.e("TEAM-NAME", "${frame.className}.${frame.methodName}:${frame.lineNumber}\n\n")
//        }
    }

    private var _teamTab = mutableStateOf(1)
    val teamTab = _teamTab

    fun setTeamTab(tabNumber: Int) {
        _teamTab.value = tabNumber
    }

    private var _inviteDialogShow = MutableLiveData(false)
    val inviteDialogShow = _inviteDialogShow
    fun setInviteDialogShow (){
        _inviteDialogShow.value = !_inviteDialogShow.value!!
    }

    val buttonContainerColor = Color (0,69,129)

    private var _bab = mutableStateOf<Boolean?>(null)
    var bab = _bab
    fun setBab (boolean: Boolean){
        _bab.value = boolean
    }

    private val _selectedTeamId = MutableLiveData(0)
    var selectedTeamId = _selectedTeamId
    fun setSelectedTeamId(id: Int) {
        _selectedTeamId.value = id
    }

    private val _selectedTeam = MutableLiveData<newTeam>(null)
    var selectedTeam = _selectedTeam

    fun setTeam(team: newTeam) {
        _selectedTeam.value = team
    }

    private val _selectedUser = MutableLiveData<newUser?>(null)
    var selectedUser = _selectedUser
    fun setUser (user: newUser) {
        _selectedUser.value = user
    }

    private val _loggedinUser = mutableStateOf(model.user)
    val loggedinUser = _loggedinUser.value

    private val _authenticatedUser = mutableStateOf(User())
    val authenticatedUser = _authenticatedUser.value

    fun setAuthenticatedUser(user : User){
        _authenticatedUser.value = user
    }

    private val _activeTask = MutableLiveData<newTask?>(null)
    val activeTask: LiveData<newTask?> = _activeTask

    fun setActiveTask(task: newTask?) {
        _activeTask.value = task
    }

    val _comments = MutableStateFlow<List<newComment>>(emptyList())
    val comments: StateFlow<List<newComment>> get() = _comments



    //---------------------------------------------------------------------------

    fun createTeam(newTeam: newTeam) = model.createTeam(newTeam, loggedinUser.uid!!)

    fun createTask(newTask: newTask, usersList: List<newUser>, history: NewHistory) = model.createTask(newTask, selectedTeamId.value!!, usersList, history)

    fun createHistory(history: NewHistory, task: newTask) = model.createHistory(history, task)

    fun createCommentTask(taskId: Int, newComment: newComment) = model.createCommentTask(taskId, newComment)

    //---------------------------------------------------------------------------

    fun getComments(taskId : Int) = model.getComments(taskId)

    fun getHistories(taskId : Int) = model.getHistories(taskId)

    fun updateTeamByTeamId(teamId: Int, newTeamData: Map<String, Any>) = model.updateTeamByTeamId(teamId, newTeamData)

    fun getTeams() = model.getTeams(loggedinUser.uid!!)

    fun getSelectedTeam(teamId: Int) = model.getSelectedTeam(teamId)

    fun getTasks(teamId: Int): Flow<List<newTask>> = model.getTasks(teamId)

    fun getTaskMembers(taskId: Int) = model.getTaskMembers(taskId)

    fun addMemberToTask(task: newTask, user: newUser) = model.addMemberToTask(task, user)

    fun removeUserFromTask(task: newTask, user: newUser) = model.removeMemberFromTask(task, user)

    fun getMembers(teamId: Int): Flow<List<newUser>> = model.getMembers(teamId)

    fun getUsersTeams(userid: String): Flow<Int> = model.getUsersTeams(userid)

    fun getUserById(id: String): Flow<newUser> = model.getMemberById(id)

//    fun getHistories(taskId: Int) = model.getHistories(taskId)

//    fun getTest() = model.getTest()

    fun deleteComment(commentId: Int, taskId: Int) = model.deleteComment(commentId, taskId)
//    fun deleteComment(commentId: Int) {
//        model.deleteComment(commentId) {
//            _comments.value = _comments.value.filter { it.commentId != commentId }
//        }
//    }
    fun assignUserToTeam(teamId: Int, userId: String) = model.assignUserToTeam(userId, teamId)


    fun getCommentsTask(taskId: Int): Flow<List<newComment>> = model.getCommentsForTask(taskId)
//    fun getCommentsTask(taskId: Int) {
//        viewModelScope.launch {
//            model.getCommentsForTask(taskId).collect { comments ->
//                Log.d("mainViewModel", "Comments fetched: ${comments.size}")
//                _comments.value = comments.distinctBy { it.commentId }
//            }
//        }
//    }
    //---------------------------------------------------------------------------

    fun updateTask(task: newTask) = model.updateTask(task)

    fun removeUserFromTeam(userid: String, teamId: Int) = model.removeUserFromTeam(userid, teamId)
}