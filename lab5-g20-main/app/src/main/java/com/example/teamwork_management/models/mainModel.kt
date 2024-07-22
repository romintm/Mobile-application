package com.example.teamwork_management.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.teamwork_management.application.mainApplication
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.newComment
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.dataClasses.relation.TaskCrossRef
import com.example.teamwork_management.dataClasses.relation.TeamCrossRef
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class mainModel (context: Context) {



    init {
        FirebaseApp.initializeApp(context)
    }

    val db = Firebase.firestore

    val user = FirebaseAuth.getInstance()
    val roomDb = mainApplication.db

    val a = context
    fun isNetworkAvailable(context: Context = a): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
//---------------------------------------------------------------------------

    // Create a new Team and connect it to the User
    fun createTeam(newteam: newTeam, uid: String) {
        db.collection("teams")
            .orderBy("teamId")
            .get()
            .addOnSuccessListener {
                newteam.teamId = (it.documents.last().get("teamId") as Long).toInt() + 1
                Log.i("FOUND", "TeamId Found.")
                db.collection("teams")
                    .add(newteam)
                    .addOnSuccessListener {
                        createTeamsFromRoomDatabase(newteam, uid)
                        Log.i("Created", "Team created.")

                        var connection = mapOf("userId" to uid, "teamId" to newteam.teamId)
                        db.collection("junction_user_team")
                            .add(connection)
                            .addOnSuccessListener {
                                Log.i("Created", "Team connected to the User.")
                            }
                            .addOnFailureListener {
                                Log.e("Error", "Team does not connect to the User.")
                            }
                    }
                    .addOnFailureListener {
                        Log.e("Error", "Team does not created.")
                    }
            }
            .addOnFailureListener {
                Log.e("Error", "TeamId does not found.")
            }
        createTeamsFromRoomDatabase(newteam, uid)
    }

    private fun createTeamsFromRoomDatabase(newteam: newTeam,uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            roomDb.teamDao().insertNewTeam(newteam)
            roomDb.teamDao().insertTeamCross(TeamCrossRef(newteam.teamId, uid))
        }
    }

    private fun createTasksFromRoomDatabase(newTask: newTask,teamId: Int,uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            roomDb.taskDao().insert(newTask)
            roomDb.taskDao().insertTaskCross(TaskCrossRef(newTask.taskId,teamId))
        }
    }

    // Create a new Task and connected it to the Team
    fun createTask(newtask: newTask, teamId: Int, usersList: List<newUser>, history: NewHistory) {
        db.collection("tasks")
            .orderBy("taskId")
            .get()
            .addOnSuccessListener {
                newtask.taskId = (it.documents.last().get("taskId") as Long).toInt() + 1
                Log.i("FOUND", "TaskId Found.")
                db.collection("tasks")
                    .add(newtask)
                    .addOnSuccessListener {
                        Log.i("Created", "Task created.")

                        var connection = mapOf("taskId" to newtask.taskId, "teamId" to teamId)
                        db.collection("junction_team_task")
                            .add(connection)
                            .addOnSuccessListener {
                                Log.i("Created", "Task connected to the Team.")
                            }
                            .addOnFailureListener {
                                Log.e("Error", "Task does not connect to the Team.")
                            }

                        for (member in usersList) {
                            var secondConnection = mapOf("userId" to member.userId, "taskId" to newtask.taskId)
                            db.collection("junction_user_task")
                                .add(secondConnection)
                                .addOnSuccessListener {
                                    Log.i("Created", "Task connected to the ${member.name + " " + member.surname}.")
                                }
                                .addOnFailureListener {
                                    Log.e("Error", "Task does not connect to the ${member.name + " " + member.surname}.")
                                }
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Error", "Task does not created.")
                    }

                db.collection("histories")
                    .orderBy("historyId")
                    .get()
                    .addOnSuccessListener {
                        history.historyId = (it.documents.last().get("historyId") as Long).toInt() + 1
                        history.taskId = newtask.taskId
                        db.collection("histories")
                            .add(history)
                            .addOnSuccessListener {
                                Log.i("Created", "History created.")
                                db.collection("junction_task_history")
                                    .add(mapOf(
                                        "historyId" to history.historyId,
                                        "taskId" to newtask.taskId
                                    ))
                            }
                    }
            }
            .addOnFailureListener {
                Log.e("Error", "TaskId does not found.")
            }
    }

    fun createCommentTask(taskId: Int, newComment: newComment) {
        db.collection("comments")
            .whereEqualTo("taskId", taskId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val nextCommentId = if (querySnapshot.isEmpty) 0 else {
                    querySnapshot.documents.maxByOrNull { it.getLong("commentId") ?: 0 }?.getLong("commentId")?.toInt()?.plus(1) ?: 0
                }
                newComment.commentId = nextCommentId

                // Add the new comment
                db.collection("comments")
                    .add(newComment)
                    .addOnSuccessListener {
                        Log.i("Created", "Comment created.")
                    }
                    .addOnFailureListener {
                        Log.e("Error", "Comment creation failed.")
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error fetching comments: $exception")
            }
    }

    fun addCommentToTask(taskId: Int, commentId: Int) {
        db.collection("tasks")
            .document(taskId.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val task = document.toObject(newTask::class.java)

                    // Modify the comments list
                    val updatedComments = task?.comments?.toMutableList() ?: mutableListOf()
                    updatedComments.add(commentId)

                    // Update the task document
                    db.collection("tasks")
                        .document(taskId.toString())
                        .update("comments", updatedComments)
                        .addOnSuccessListener {
                            Log.i("Updated", "Comment added to task.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Error", "Error updating task: $e")
                        }
                } else {
                    Log.e("Error", "Task document not found.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Error", "Error fetching task document: $e")
            }
    }


    //Create a new History for a task
    fun createHistory (history: NewHistory, task: newTask) {
        db.collection("histories")
            .orderBy("historyId")
            .get()
            .addOnSuccessListener {
                history.historyId = (it.documents.last().get("historyId") as Long).toInt() + 1
                history.taskId = task.taskId
                db.collection("histories")
                    .add(history)
                    .addOnSuccessListener {
                        Log.i("Created", "History created.")
                        db.collection("junction_task_history")
                            .add(mapOf(
                                "historyId" to history.historyId,
                                "taskId" to task.taskId
                            ))
                    }
            }
    }
    fun assignUserToTeam(userId: String, teamId: Int){
        val junctionUserTeamRef = db.collection("junction_user_team")

        // Query to check if the document already exists
        junctionUserTeamRef
            .whereEqualTo("teamId", teamId)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Document does not exist, proceed with adding a new one
                    val connection = mapOf("teamId" to teamId, "userId" to userId)
                    junctionUserTeamRef
                        .add(connection)
                        .addOnSuccessListener {
                            Log.i("Created", "User connected to the Team.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Error", "Failed to add connection: ${e.message}")
                        }
                } else {
                    // Document already exists with the same userId and teamId
                    Log.i("Exists", "User already connected to the Team.")
                    // Handle as needed, maybe update or inform user
                }
            }
            .addOnFailureListener { e ->
                Log.e("Error", "Failed to check for existing connection: ${e.message}")
            }

//        var connection = mapOf("teamId" to teamId, "userId" to userId)
//        db.collection("junction_user_team")
//            .add(connection)
//            .addOnSuccessListener {
//                Log.i("Created", "User connected to the Team.")
//            }
//            .addOnFailureListener {
//                Log.e("Error", "User does not connect to the Team.")
//            }
    }


//---------------------------------------------------------------------------
    //Get a member
//    fun getMemberById(id: String): newUser? {
//        var member: newUser? = null
//        var getMember = db.collection("users")
//            .whereEqualTo("userId", id)
//            .get()
//            .addOnSuccessListener {
//                member = (it.documents.last().toObject(newUser::class.java))
//                Log.i("FOUND", "User Found.")
//            }
//            .addOnFailureListener {
//                Log.e("ERROR", "User does not found.")
//            }
//        return member
//    }

    fun getMemberById(id: String) = callbackFlow<newUser> {
        lateinit var member: newUser
        var getMember = db.collection("users")
            .whereEqualTo("userId", id)
            .addSnapshotListener{
                res, err ->
                if (res != null) {
                    member = (res.documents.last().toObject(newUser::class.java)!!)
                    Log.i("FOUND", "User Found.")
                    trySend(member)
                } else {
                    Log.e("ERROR", "User does not found.")
                }
            }
        awaitClose { getMember.remove() }
    }

    // Get Teams of logged in user
    fun getTeams(uid: String) = callbackFlow<List<newTeam>> {
        var finalTeams = mutableListOf<newTeam>()
        if (isNetworkAvailable()){
            val usersTeams = db.collection("junction_user_team")
                .whereEqualTo("userId", uid)
                .addSnapshotListener{
                        r, e ->
                    if (r != null){
                        for (team in r) {
                            db.collection("teams")
                                .whereEqualTo("teamId", team.get("teamId"))
                                .addSnapshotListener {
                                        r, e ->
                                    if (r != null) {
                                        r.documents.forEach { finalTeams.add(it.toObject(newTeam::class.java)!!) }
                                    }

                                    trySend(finalTeams).isSuccess
                                }
                        }
                    } else {
                        Log.e("ERROR", e.toString())

                    }
                }
            awaitClose{usersTeams.remove()}
        } else {
            fetchTeamsFromRoomDatabase(uid,this)
            }
            awaitClose()
    }
    private suspend fun fetchTeamsFromRoomDatabase(uid: String, sendChannel: SendChannel<List<newTeam>>) {
        try {
            val userWithTeam = roomDb.teamDao().getTeamsOfUser(uid)
            val teamsFromRoom = userWithTeam.teams
            withContext(Dispatchers.Main) {
                sendChannel.trySend(teamsFromRoom).isSuccess
            }
        } catch (ex: Exception) {
            Log.e("ERROR", ex.toString())
        }
    }
    // Get the selected team
    fun getSelectedTeam (teamId: Int) = callbackFlow<newTeam> {
        if (isNetworkAvailable()){
        val listener = db.collection("teams")
            .whereEqualTo("teamId", teamId)
            .addSnapshotListener{
                res, e ->
                if (res != null) {
                    trySend(res.documents.last().toObject(newTeam::class.java)!!)
                }
            }
        awaitClose { listener.remove() }
    }else {
            fetchTeamByIdFromRoomDatabase(teamId,this)
        }
        awaitClose()

    }

    // Function to update a team document in Firestore based on teamId
    fun updateTeamByTeamId(teamId: Int, newTeamData: Map<String, Any>) {
        // Query to find the document(s) with matching teamId
        try {
            db.collection("teams")
                .whereEqualTo("teamId", teamId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        // Update each document found
                        document.reference.update(newTeamData)
                            .addOnSuccessListener {
                                // Handle success if needed
                                Log.i("Success","DocumentSnapshot successfully updated!")
                            }
                            .addOnFailureListener { e ->
                                // Handle error
                                Log.e("ERROR","Error updating document: $e")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle error
                    Log.e("ERROR", "Error getting documents: $e")
                }
        } catch(e: Exception){
            Log.e("ERROR", e.toString())
        }
    }
    private suspend fun fetchTeamByIdFromRoomDatabase(teamId: Int, sendChannel: SendChannel<newTeam>) {
        try {
            val teamById = roomDb.teamDao().getTeamById(teamId)
            teamById?.let {
                sendChannel.trySend(it).isSuccess
            }
        } catch (ex: Exception) {
            Log.e("ERROR", ex.toString())
        }
    }
    // Get the tasks of the selected team
    fun getTasks(teamId: Int) = callbackFlow<List<newTask>> {
        if (isNetworkAvailable()){
        val finalTasks = mutableListOf<newTask>()
        val teamTasks = db.collection("junction_team_task")
            .whereEqualTo("teamId", teamId)
            .addSnapshotListener{
                    r, e ->
                if (r!=null) {
                    for (task in r){
                        db.collection("tasks")
                            .whereEqualTo("taskId", task.get("taskId"))
                            .addSnapshotListener{
                                r, e ->
                                if (r != null){
                                    r.documents.forEach { finalTasks.add(it.toObject(newTask::class.java)!!) }
                                }
                                trySend(finalTasks)
                            }
                    }
                } else {
                    Log.e("ERROR", e.toString())
                    trySend(emptyList())
                }
            }
        awaitClose{teamTasks.remove()}
    }else {
           fetchTasksFromRoomDatabase(teamId,this)
        }
        awaitClose()

    }
    private suspend fun fetchTasksFromRoomDatabase(teamId: Int, sendChannel: SendChannel<List<newTask>>) {
        try {
            val taskWithTeam = roomDb.taskDao().getTaskOfTheTeam(teamId)
            val tasksFromRoom = taskWithTeam.tasks
            withContext(Dispatchers.Main) {
                sendChannel.trySend(tasksFromRoom).isSuccess
            }
        } catch (ex: Exception) {
            Log.e("ERROR", ex.toString())
        }
    }

    // Get members of a specofic Task
    fun getTaskMembers(taskId: Int) = callbackFlow<List<newUser>> {
        val finalMembers = mutableListOf<newUser>()
        val members = db.collection("junction_user_task")
            .whereEqualTo("taskId", taskId)
            .addSnapshotListener{
                res, err ->
                if (res!=null){
                    for (member in res){
                        db.collection("users")
                            .whereEqualTo("userId", member.get("userId"))
                            .addSnapshotListener {
                                res, err ->
                                if (res!=null){
                                    res.forEach { finalMembers.add(it.toObject(newUser::class.java)) }
                                    trySend(finalMembers)
                                } else {
                                    Log.e("Error", "Could not find any Member.")
                                }
                            }
                    }
                } else {
                    Log.e("Error", "Task does not found.")
                }
            }
        awaitClose { members.remove() }
    }

    //Get comments of a task
    fun getComments(taskId: Int)= callbackFlow<List<newComment>>{
        val comments : MutableList<newComment> = mutableListOf()
            db.collection("comments")
            .whereEqualTo("taskId", taskId)
            .addSnapshotListener{
                    r, e ->
                if (r != null){
                    r.documents.forEach { comments.add(it.toObject(newComment::class.java)!!) }
                }
                trySend(comments)
            }
    }

//    fun getHistories(taskId: Int)= callbackFlow<List<NewHistory>>{
//        val histories : MutableList<NewHistory> = mutableListOf()
//        db.collection("histories")
//            .whereEqualTo("taskId", taskId)
//            .addSnapshotListener{
//                    r, e ->
//                if (r != null){
//                    r.documents.forEach { histories.add(it.toObject(NewHistory::class.java)!!) }
//                }
//                trySend(histories)
//            }
//    }

    // Get members of a specific Team
    fun getMembers(teamId: Int) = callbackFlow<List<newUser>> {
        if (isNetworkAvailable()){
        var members = mutableListOf<newUser>()

        var allMembers = db.collection("junction_user_team")
            .whereEqualTo("teamId", teamId)
            .addSnapshotListener{
                r, e ->
                if(r != null){
                    for (user in r){
                        db.collection("users")
                            .whereEqualTo("userId", user.get("userId"))
                            .addSnapshotListener{
                                r, e ->
                                if (r != null) {
                                    r.documents.forEach { members.add(it.toObject(newUser::class.java)!!) }
                                    trySend(members)
                                } else {
                                    Log.e("ERROR", "User does not find.")
                                    trySend(emptyList())
                                }
                            }
                    }
                } else {
                    Log.e("ERROR", "Team does not find.")
                    trySend(emptyList())
                }
            }
        awaitClose {
            allMembers.remove() }
        }else {
                fetchMembersFromRoomDatabase(teamId,this)
            }
            awaitClose()

        }
    private suspend fun fetchMembersFromRoomDatabase(teamId: Int, sendChannel: SendChannel<List<newUser>>) {
        try {
            val usersWithTeam = roomDb.userDao().getUsersOfTeam(teamId)
            val membersFromRoom = usersWithTeam.users
            withContext(Dispatchers.Main) {
                sendChannel.trySend(membersFromRoom).isSuccess
            }
        } catch (ex: Exception) {
            Log.e("ERROR", ex.toString())
        }
    }

    fun getUsersTeams(usedid: String) = callbackFlow<Int> {
        if (isNetworkAvailable()){
        val teamnumber = db.collection("junction_user_team")
            .whereEqualTo("userId", usedid)
            .addSnapshotListener{
                r, e ->
                if (r!=null){
                    trySend(r.documents.size)
                } else{
                    Log.e("ERROR", "Users teams did not found.")
                }
            }
        awaitClose { teamnumber.remove() }
    }else {
           fetchusersTeamFromRoomDatabase(usedid,channel)
        }
        awaitClose()

    }

    private suspend fun fetchusersTeamFromRoomDatabase(usedid:String, sendChannel: SendChannel<Int>) {
        try {
            val usersWithTeam = roomDb.teamDao().getTeamsOfUser(usedid)
            val membersFromRoom = usersWithTeam.teams.size
            withContext(Dispatchers.Main) {
                sendChannel.trySend(membersFromRoom).isSuccess
            }
        } catch (ex: Exception) {
            Log.e("ERROR", ex.toString())
        }
    }

    fun getHistories(taskId: Int) = callbackFlow<List<NewHistory>> {
        var historyList: MutableList<NewHistory> = mutableListOf()
        db.collection("junction_task_history")
            .whereEqualTo("taskId", taskId)
            .addSnapshotListener{
                res, err ->
                if (res!=null){
                    for (history in res) {
                        db.collection("histories")
                            .whereEqualTo("historyId", history.get("historyId"))
                            .addSnapshotListener {
                                res, err ->
                                if (res!=null){
                                    res.forEach { historyList.add(it.toObject(NewHistory::class.java)) }
                                }
                                trySend(historyList)
                            }
                    }

                }
            }
        awaitClose()
    }
    //---------------------------------------------------------------------------

    //Add a new member to a task
    fun addMemberToTask(task: newTask, user: newUser) {
        val nuser = mapOf("userId" to user.userId, "taskId" to task.taskId)
        db.collection("junction_user_task")
            .add(nuser)
            .addOnSuccessListener {
                Log.i("Added", "User ${user.userId} add to task ${task.taskId}")
            }
            .addOnFailureListener {
                Log.e("ERROR", "Could not add User ${user.userId} to task ${task.taskId}")
            }
    }

    //---------------------------------------------------------------------------

    //Remove a member from a task
    fun removeMemberFromTask(task: newTask, user: newUser) {
        val nuser = mapOf("userId" to user.userId, "taskId" to task.taskId)
        db.collection("junction_user_task")
            .whereEqualTo("taskId", task.taskId)
            .whereEqualTo("userId", user.userId)
            .get()
            .addOnSuccessListener {
                it.forEach {
                    db.collection("junction_user_task")
                    .document(it.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.i("Removed", "User removed successfully.")
                        }
                }
            }
            .addOnFailureListener {
                Log.e("ERROR", "Could not add User ${user.userId} to task ${task.taskId}")
            }
    }

    //---------------------------------------------------------------------------

    //Update a task
    fun updateTask(task: newTask) {
        db.collection("tasks")
            .whereEqualTo("taskId", task.taskId)
            .get()
            .addOnSuccessListener {
                it.forEach {
                    db.collection("tasks")
                        .document(it.id)
                        .update(mapOf(
                            "title" to task.title,
                            "category" to task.category,
                            "description" to task.description,
                            "dueDate" to task.dueDate,
                            "state" to task.state
                        ))
                        .addOnSuccessListener {
                            Log.i("UPDATED", "Task ${task.taskId} updated successfully.")
                        }
                        .addOnFailureListener{
                            Log.e("ERROR", "Task does not updated.")
                        }
                }
            }
            .addOnFailureListener {
                Log.e("ERROR", "Task does not found.")
            }
    }

    //get list of comments
    fun getCommentsForTask(taskId: Int) = callbackFlow<List<newComment>> {
        db.collection("comments")
            .whereEqualTo("taskId", taskId)
            .addSnapshotListener { snapshot, exception ->
                val comments = mutableListOf<newComment>()
                if (snapshot != null) {
                    snapshot.documents.forEach { comments.add(it.toObject(newComment::class.java)!!) }
                    trySend(comments)
                } else {
                    Log.e("ERROR", "Could not find any Comment")
                }

            }
        awaitClose()
    }

    private suspend fun fetchTaskByIdFromRoomDatabase(taskId: Int, sendChannel: SendChannel<newTask>) {
        try {
            val taskById = roomDb.taskDao().getTaskById(taskId)
            taskById?.let {
                sendChannel.trySend(it).isSuccess
            }
        } catch (ex: Exception) {
            Log.e("ERROR", ex.toString())
        }
    }

    fun getTasksCmNumber(taskId: Int) = callbackFlow {
        val db = FirebaseFirestore.getInstance()
        val listenerRegistration = db.collection("comments")
            .whereEqualTo("taskId", taskId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val commentCount = snapshot?.size() ?: 0
                trySend(commentCount).isSuccess
            }
        awaitClose { listenerRegistration.remove() }
    }

    fun deleteComment(commentId: Int, taskId: Int) {
        db.collection("comments")
            .whereEqualTo("taskId",  taskId)
            .whereEqualTo("commentId", commentId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("comments")
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.i("Deleted", "Comment ${commentId} deleted.")
                        }
                }
            }
    }


    fun removeUserFromTeam(userId: String, teamId: Int) {
        db.collection("junction_user_team")
            .whereEqualTo("userId", userId)
            .whereEqualTo("teamId", teamId)
            .get()
            .addOnSuccessListener {
                for (team in it) {
                    db.collection("junction_user_team")
                        .document(team.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.i("Deleted", "Team ${teamId} deleted from User ${userId} teams.")
                        }
                    db.collection("junction_team_task")
                        .whereEqualTo("teamId", teamId )
                        .get()
                        .addOnSuccessListener {
                            for (task in it) {
                                db.collection("junction_user_task")
                                    .whereEqualTo("taskId", task.get("taskId"))
                                    .whereEqualTo("userId",userId)
                                    .get()
                                    .addOnSuccessListener {
                                        for (task in it) {
                                            db.collection("junction_user_task")
                                                .document(task.id)
                                                .delete()
                                                .addOnSuccessListener {
                                                    Log.i("Deleted", "User ${userId} deleted from Task ${task.get("taskId")}.")
                                                }
                                        }
                                    }
                            }
                        }
                }
            }
    }
}