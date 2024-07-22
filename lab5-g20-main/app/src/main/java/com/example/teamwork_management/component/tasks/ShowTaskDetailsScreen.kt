package com.example.teamwork_management.component.tasks

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teamwork_management.dataClasses.Comment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.TaskHistoryElement
import com.example.teamwork_management.dataClasses.newComment
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.viewModels.TasksViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val dateFormat = SimpleDateFormat("yyyy-MM-dd")
@Composable
fun PresentTaskDetailView(vm: mainViewModel, navController : NavController
){
    vm.setBab(false)
    vm.setLeftButton("BackToTaskList", Icons.Default.ArrowBackIosNew, "BackToTaskList"){
        navController.popBackStack()
        vm.setBab(true)
        vm.setTeamTab(1)
        vm.setRightButton("", null, "") {}
    }
    vm.setRightButton("EditTask", Icons.Default.Edit, "EditTask") {
        navController.navigate("editTask")
    }
    vm.setTeamTab(0)
    BoxWithConstraints {
        if (this.maxHeight > maxWidth){
            Column (
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                taskDetails(vm, h = 0.3f)
                Spacer(modifier = Modifier.height(16.dp))
                tabView(vm)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ){
                Row (
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (Modifier.fillMaxWidth(1f)){
                        taskDetails(vm, h = 0.3f)
                        Spacer(modifier = Modifier.height(16.dp))
                        tabView(vm)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun taskDetails(vm: mainViewModel, h: Float) {
    val members by vm.getTaskMembers(vm.activeTask.value?.taskId!!).collectAsState(initial = listOf())
    val activeTask by vm.activeTask.observeAsState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ){
        Column(modifier = Modifier.fillMaxSize()){
            Text(activeTask?.title ?: "Task title default", style = MaterialTheme.typography.headlineMedium, color = Color(color= 0xFF026B92))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("Status: ${activeTask?.state ?: "default state"}")

//                    val members by vm.getTaskMembers(vm.activeTask.value!!.taskId).collectAsState(initial = listOf())
                    if (members.isNotEmpty()) {
                        val memberString = members.joinToString(", ") { "${it.name} ${it.surname}" }

                        Text("Assignee: $memberString")
                    } else {
                        Text("")
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Due date: ${activeTask?.dueDate ?: "01/01/2000"}")
                    Text("Category: ${activeTask?.category ?: "default category"}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Description:",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp // Change the font size as needed
                ),
                color = Color(color= 0xFF026B92)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .border(1.dp, Color(0xFF0296CD), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                TextField(
                    value = activeTask?.description ?: "Default description",
                    onValueChange = { /* No-op */ },
                    minLines = 5,
                    maxLines = 5, // Set the maximum number of lines
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                    textStyle = TextStyle(fontSize = 14.sp), // Adjust text size as needed
                    readOnly = true, // Make the text field read-only
                    singleLine = false // Allow multiple lines

                )
            }
        }
    }
}

@Composable
fun tabView(vm: mainViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clip(RoundedCornerShape(50))
                .padding(1.dp)
        ) {
            Tab(
                modifier = if (selectedTabIndex == 0) {
                    Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(color = 0xFF026B92))
                }
                else {
                    Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                },
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            ) {
                Text("Comment", color = if (selectedTabIndex == 0) Color.White else Color(color= 0xFF026B92))
            }
            Tab(
                modifier = if (selectedTabIndex == 1) {
                    Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(color = 0xFF026B92))
                }
                else {
                    Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                },
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 }
            ) {
                Text("History", color = if (selectedTabIndex == 1) Color.White else Color(color= 0xFF026B92))
            }
        }

        // Content of each tab
        when (selectedTabIndex) {
            0 -> CommentsSectionContent(vm)
            1 -> HistorySectionContent(vm)
        }
    }
}

@Composable
fun HistorySectionContent(vm: mainViewModel) {
    HistoryList(vm)
}

@Composable
fun HistoryList(vm: mainViewModel) {
    val historyList by vm.getHistories(vm.activeTask.value?.taskId!!)
        .map { list ->
            list.sortedByDescending { historyElement ->
                LocalDateTime.parse(historyElement.date, DateTimeFormatter.ISO_DATE_TIME)
            }
        }
        .collectAsState(initial = listOf())
    LazyColumn(
        modifier = Modifier
            .height(300.dp)
    ) {
        itemsIndexed(historyList) { index, historyElement ->
            // Each comment in the list will be rendered here
            HistoryItem(he= historyElement, vm)
        }
    }
}

@Composable
fun HistoryItem(he: NewHistory, mainvm: mainViewModel) {
    val user by mainvm.getUserById(he.author).collectAsState(initial = newUser())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(1.dp, Color(0xFF0296CD)),
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .padding(3.dp)
        ){
            Text(he.action)
        }
        Row(
            modifier = Modifier
                .padding(3.dp)
        ){
            Text(text = "Author: " + user.name + " " + user.surname)
        }
        Row(
            modifier = Modifier
                .padding(3.dp)
        ){
            Text(text = "Date: " + he.date.split("T")[0] + " - " + he.date.split("T", ".")[1])
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSectionContent(vm: mainViewModel) {

    val datesFormat = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val loggedInUserName = vm.authenticatedUser.name
    val commentsState by vm.getCommentsTask(vm.activeTask.value?.taskId!!).collectAsState(initial = listOf())


    Column {
        var commentText by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text input field
            TextField(
                value = commentText,
                onValueChange = { commentText = it },
                modifier = Modifier
                    .weight(1f) // Occupy available space
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20))
                    .border(1.dp, Color(0xFF0296CD), RoundedCornerShape(20)),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                placeholder = { Text("Enter your comment") }
            )

            // Button
            Button(
                onClick = {
//                    vm.addComment(Comment(vm.activeTask.value?.comments?.size ?: 0, vm.activeTask.value?.id ?: 0, "Author 1", dateFormat.parse("05/05/2024"), commentText))
                    val taskId = vm.activeTask.value?.taskId ?: return@Button
                    val currentDate = LocalDate.now()
                    val formattedDate = currentDate.format(datesFormat)
                    val newComment = newComment(
                        taskId= taskId,
                        author = vm.loggedinUser.uid!!,
                        date = formattedDate,
                        comment = commentText
                    )
                    vm.createCommentTask(taskId, newComment)
                    commentText = "" // Clear text input after clicking the button

                },
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                enabled = commentText.isNotBlank(), // Disable button if commentText is empty
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(color= 0xFF026B92),
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = "Add Comment")
            }
        }
        CommentsList(vm, commentsState)
    }
}

@Composable
fun CommentsList(vm: mainViewModel, commentsState: List<newComment>) {
    LazyColumn(
        modifier = Modifier
            .height(300.dp)
    ) {
        items(commentsState.sortedByDescending { it.commentId }) { comment ->
            CommentItem(
                comment = comment,
                vm = vm,
                onDeleteClicked = {
                    vm.deleteComment(comment.commentId, vm.activeTask.value?.taskId!!)
                }
            )
        }
    }
}

@Composable
fun CommentItem(comment: newComment, vm:mainViewModel, onDeleteClicked: (Int) -> Unit) {
    val user by vm.getUserById(comment.author).collectAsState(initial = newUser())
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(1.dp, Color(0xFF0296CD)),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.padding(8.dp) // Add padding around the content
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Author: ${user.name + " " + user.surname}")
                }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = {onDeleteClicked(comment.commentId)},
                        modifier = Modifier.size(23.dp) // Adjust the size here
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Comment")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: ${comment.date}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Comment:",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Color(0xFF0296CD)
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(comment.comment)
            }
        }
    }
}