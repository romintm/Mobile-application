package com.example.teamwork_management.component


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LineStyle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.teamwork_management.component.tasks.dateFormat
import com.example.teamwork_management.dataClasses.NewHistory
import com.example.teamwork_management.dataClasses.Status
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.viewModels.TasksViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditTaskScreen(
    mainvm: mainViewModel,
    navController: NavController,
    mainPageNavController: NavController
) {

    mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
        mainvm.setRightButton("", null, "") {}
        mainvm.setBab(false)
        mainvm.setTeamTab(1)
        mainvm.setTitle(mainvm.selectedTeam.value!!.name)
        navController.popBackStack()
    }
    mainvm.setRightButton("", null, ""){}

    val task by mainvm.activeTask.observeAsState(initial = newTask())

    var title by remember { mutableStateOf(task?.title) }
    var titleError by remember { mutableStateOf("") }
    var newTitle by remember { mutableStateOf(title) }

    var description by remember { mutableStateOf(task?.description) }
    var newDescription by remember { mutableStateOf(description) }

    var tag by remember { mutableStateOf("") }

    var dueDate by remember { mutableStateOf(task?.dueDate) }
    val newDate = rememberDatePickerState()

    var status by remember { mutableStateOf(task?.state) }
    var newStatus by remember { mutableStateOf(status) }

    var category by remember { mutableStateOf(task?.category) }
    var newCategory by remember { mutableStateOf(category) }



    val memberAssign by mainvm.getTaskMembers(task?.taskId!!).collectAsState(initial = listOf())
    val members by mainvm.getMembers(mainvm.selectedTeamId.value!!).collectAsState(initial = listOf())
    val selectedMembers by mainvm.getTaskMembers(task?.taskId!!).collectAsState(initial = listOf())
    var selectedMembersError by remember { mutableStateOf("") }


    fun checkTitle() {
        if (newTitle?.isBlank()!!) {
            titleError = "Title Can not be empty"
        } else {
            titleError = ""
        }
    }

    fun checkMember() {
        if (selectedMembers.isEmpty()) {
            selectedMembersError = "At least one member need to be assigned"
        } else {
            selectedMembersError = ""
        }
    }

    var descSize by remember {
        mutableStateOf(0.23f)
    }

    fun changeDescription() {
        if (titleError.isNotBlank() && selectedMembersError.isNotBlank()){
            descSize = 0.2f
        } else {
            descSize = 0.24f
        }
    }

    var date by remember { mutableStateOf("Date") }



    var showDatePicker by remember { mutableStateOf(false) }
    var membersDialog by remember { mutableStateOf(false) }
    var dropdownExpand by remember { mutableStateOf(false) }


//    var members by remember {
//        mutableStateOf(setOf("Jodene Ruslan", "Deodato Debbie", "Shalim Marin",
//            "Ladislao Gabriella", "Léana Corinna", "Payne Junon",
//            "Tau Gjergj", "Gardenia Jonny", "Mojmir Chimezie", "Jinan Iudris"))
//    }

    fun setStatus(n: String) {
        newStatus = ""
        var a: String = Status.valueOf(n).toString()
        if (a == "To_Verify") {
            newStatus += "To Verify"
        } else if (a == "In_Progress") {
            newStatus += "In Progress"
        } else if(a ==  "Completed") {
            newStatus += "Completed"
        } else{
            newStatus += Status.valueOf(n)
        }
    }

//    fun saveChanges() {
//        vm.updateActiveTaskTitle(title)
//        vm.updateActiveTaskDescription(description)
//        vm.updateActiveTaskTag(tag)
//        vm.updateActiveTaskCategory(category)
//        vm.updateActiveTaskDueDate(dateFormat.parse(dueDate.toString()).toString())
//        vm.updateActiveTaskState(status)
//        vm.updateActiveTaskAssignMembers(memberAssign?.toList()?.toMutableList())
//    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(255, 255, 255))
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Text(
            text = "Name of the Task ",
            modifier = Modifier.align(Alignment.Start),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                value = newTitle!!,
                onValueChange = { newTitle = it } ,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Color(232, 232, 232), shape = RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(246, 246, 246),
                    unfocusedPlaceholderColor = Color(155, 155, 155),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(246, 246, 246),
                    focusedPlaceholderColor = Color(199, 199, 199),
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Name of the task") },
                singleLine = true
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
//Description of the task
        Text(
            text = "Description of the task",
            modifier = Modifier.align(Alignment.Start),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(descSize),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                value = newDescription!!,
                onValueChange = { newDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, color = Color(232, 232, 232), shape = RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(246, 246, 246),
                    unfocusedPlaceholderColor = Color(155, 155, 155),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(246, 246, 246),
                    focusedPlaceholderColor = Color(199, 199, 199),
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Description of the task") },
                maxLines = 10
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        //Date of the task
        Text(
            text = "Date of the task",
            modifier = Modifier.align(Alignment.Start),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = Color(232, 232, 232), shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(246, 246, 246))
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

           if (showDatePicker) {
               DatePickerDialog(onDismissRequest = {  }, confirmButton = {
                   Button(onClick = { showDatePicker = false
                       var a = newDate.selectedDateMillis
                       date=Instant.ofEpochMilli(a!!).atZone(ZoneId.of("UTC")).toLocalDate().toString()
                       dueDate = date
                   }) {
                       Text(text = "OK")
                   }
               },
                   dismissButton = {
                       Button(onClick = { showDatePicker = false }) {
                           Text(text = "Cancel")
                       }
                   }){
                   DatePicker(state = newDate)
               }
            }

            Text(
                modifier = Modifier
                    .width(200.dp),
                text = dueDate.toString(),
                fontSize = 16.sp,
                color = Color(155, 155, 155)
            )

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .width(80.dp)
                    .padding(0.dp),
            ){
                IconButton(onClick = { showDatePicker = true }
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Date Selector")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        //Category of the task
        Text(
            text = "Category of the task",
            modifier = Modifier.align(Alignment.Start),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                value = newCategory!!,
                onValueChange = { newCategory = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Color(232, 232, 232), shape = RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(246, 246, 246),
                    unfocusedPlaceholderColor = Color(155, 155, 155),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(246, 246, 246),
                    focusedPlaceholderColor = Color(199, 199, 199),
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Category") },
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        //Members of the task
        Text(
            text = "Assigned member for the task",
            modifier = Modifier.align(Alignment.Start),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = Color(232, 232, 232), shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(246, 246, 246))
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
            ) {
                if (selectedMembers.isEmpty()){
                    Text(
                        modifier = Modifier
                            .width(200.dp),
                        text = "Assigned members",
                        fontSize = 16.sp,
                        color = Color(155, 155, 155)
                    )
                } else {
                    LazyRow {

                        itemsIndexed(selectedMembers.toList()) { index, member ->
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .padding(end = 4.dp),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(246,246,246),
                                    disabledContentColor = Color(80,80,80)
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                ),
                                onClick = {
                                },
                                enabled = false
                            ) {
                                Text(
                                    text = member.name + " " + member.surname,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                )
                            }
                        }
                    }
                }
            }

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .width(80.dp)
                    .padding(0.dp),
            ){
                IconButton(onClick = { membersDialog = !membersDialog }
                ) {
                    Icon(Icons.Filled.LineStyle, contentDescription = "Members List")
                }
            }

            if (membersDialog) {
                Dialog(onDismissRequest = { membersDialog = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .fillMaxWidth()
                            .background(Color.White),
                    ) {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Row (
                                modifier = Modifier
                                    .fillMaxHeight(0.25f)
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .drawBehind {
                                        drawLine(
                                            color = Color.Black,
                                            start = Offset(0f, size.height),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = 2.dp.toPx()
                                        )
                                    },
                            ){
                                Column (
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row (modifier = Modifier.padding(bottom = 8.dp)) {
                                        Text(text = "Selected members", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                                    }
                                    Row {
                                        LazyRow {
                                            itemsIndexed(selectedMembers.toList()) { index, member ->
                                                ElevatedCard (
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp)
                                                        .fillMaxHeight(0.6f),
                                                    shape = RoundedCornerShape(4.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color(246,246,246)
                                                    ),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 6.dp
                                                    ),
                                                    onClick = {
                                                        mainvm.removeUserFromTask(task!!, member)
                                                    }
                                                ) {
                                                    Text(
                                                        text = member.name + " " + member.surname,
                                                        fontSize = 18.sp,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                top = 8.dp,
                                                                start = 8.dp,
                                                                end = 8.dp
                                                            ),
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Row (
                                modifier = Modifier
                                    .fillMaxHeight(0.85f)
                                    .padding(16.dp),
                            ){
                                LazyColumn (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    itemsIndexed(members.filter { !selectedMembers.contains(it) }) { index, member ->
                                        ElevatedCard (
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(4.dp)
                                                .height(40.dp),
                                            shape = RoundedCornerShape(4.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(246,246,246)
                                            ),
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = 6.dp
                                            ),
                                            onClick = {
                                                mainvm.addMemberToTask(task!!, member)
                                                println(selectedMembers)
                                            }
                                        ) {
                                            Text(
                                                text = member.name + " " + member.surname,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 8.dp, start = 8.dp),
                                            )
                                        }
                                    }
                                }
                            }

                            Row (
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(onClick = { membersDialog = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(151, 203, 220),
                                        contentColor = Color(255, 255, 255)
                                    ),
                                    modifier = Modifier
                                        .height(45.dp)
                                        .width(100.dp)
                                ) {
                                    Text(text = "Close")
                                }
                                Button(onClick = {
//                                    selectedMembers.forEach { memberAssign.add(it) }
                                    membersDialog = false
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0, 69, 129),
                                        contentColor = Color(255, 255, 255)
                                    ),
                                    modifier = Modifier
                                        .height(45.dp)
                                        .width(100.dp)
                                ) {
                                    Text(text = "Ok")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



        //Status of the task
        Text(
            text = "Status of the task",
            modifier = Modifier.align(Alignment.Start),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = Color(232, 232, 232), shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(246, 246, 246))
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                text = newStatus!!,
                fontSize = 16.sp,
                color = Color(155, 155, 155)
            )

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .width(80.dp),
            ){
                IconButton(onClick = { dropdownExpand = !dropdownExpand }
                ) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Members List")
                }

                DropdownMenu(expanded = dropdownExpand,
                    onDismissRequest = { dropdownExpand = false },
                    modifier = Modifier
                        .background(Color(246,246,246))
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Open") },
                        onClick = { setStatus("Open")
                            dropdownExpand = false },

                        )
                    DropdownMenuItem(
                        text = { Text(text = "Completed") },
                        onClick = { setStatus("Completed")
                            dropdownExpand = false },
                    )
                    DropdownMenuItem(
                        text = { Text(text = "In Progress") },
                        onClick = { setStatus("In_Progress")
                            dropdownExpand = false },
                    )
                    DropdownMenuItem(
                        text = { Text(text = "To Verify") },
                        onClick = { setStatus("To_Verify")
                            dropdownExpand = false },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
// Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                    mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome"
                    ) {
                        mainPageNavController.popBackStack()
//                        navController.popBackStack()
//                        mainvm.setTeamTab(1)
                    }
                    mainvm.setRightButton("EditTask", Icons.Default.Edit, "EditTask") {
                        navController.navigate("editTask")
                        mainvm.setLeftButton("", null, ""){}
                        mainvm.setRightButton("", null, ""){}
                    }
                    mainvm.setBab(false)
                    //mainvm.setTeamTab(1)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(151, 203, 220),
                    contentColor = Color(255, 255, 255)
                ),
                modifier = Modifier
                    .height(45.dp)
                    .width(100.dp)
            ) {
                Text(text = "Cancel")
            }

            Button(
                onClick = {
                    checkTitle()
                    checkMember()
                    changeDescription()
                    if (titleError.isBlank()){
                        val newtask = newTask(
                            taskId = task?.taskId!!,
                            title = newTitle!!,
                            description = newDescription,
                            tag = tag,
                            category = newCategory,
                            dueDate = dueDate,
                            state = newStatus!!)
                        mainvm.updateTask(
                            newtask,
                        )
                        mainvm.setActiveTask(newtask)
                        mainvm.createHistory(
                            NewHistory(
                                author = mainvm.loggedinUser.uid!!,
                                action = "Task Updated",
                                date = LocalDateTime.now().toString(),
                                taskId = null,
                                historyId = null
                            ),
                            task!!
                        )
                        navController.popBackStack()
                        mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome"
                        ) {
                            mainPageNavController.popBackStack()
                        }
                        mainvm.setRightButton("EditTask", Icons.Default.Edit, "EditTask") {
                            navController.navigate("editTask")
                            mainvm.setLeftButton("", null, ""){}
                            mainvm.setRightButton("", null, ""){}
                        }
                        mainvm.setBab(false)
                        mainvm.setTeamTab(1)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0, 69, 129),
                    contentColor = Color(255, 255, 255)
                ),
                modifier = Modifier
                    .height(45.dp)
                    .width(100.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}



