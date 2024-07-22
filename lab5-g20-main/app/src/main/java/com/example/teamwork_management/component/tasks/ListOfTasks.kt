package com.example.teamwork_management.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.teamwork_management.dataClasses.newTask
import com.example.teamwork_management.viewModels.TasksViewModel
import com.example.teamwork_management.viewModels.mainViewModel


@Composable
fun TaskItem(task: newTask,
             navController: NavController,
             onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(width = 1.dp, color = Color(0xFF0296CD), shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ){
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = task.title,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.85f)
                        )
                        Spacer(modifier = Modifier.weight(0.10f)) // Add space between the rectangle and the title
                        // Small rectangle
                        Box(
                            modifier = Modifier
                                .weight(0.25f) // Box occupies 15% of the width
                                .fillMaxHeight()
                                .background(color = Color(0xFF0296CD), shape = CircleShape)
                                .padding(vertical = 0.5.dp) // Adjust the vertical padding to make the box smaller
                                .aspectRatio(5f), // Set the aspect ratio to make the box square,
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = task.category!!, // Text to be displayed inside the rectangle
                                color = Color.White,
                                fontSize = 8.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Add space between the rows
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        /*Text(
                            text = "Assigned: ${task.assignedMembers.last()}",
                            style = TextStyle(fontSize = 10.sp),
                            modifier = Modifier.weight(0.37f)
                        )*/
                        Spacer(modifier = Modifier.weight(0.08f)) // Add space between the assigned name and the date
                        Text(
                            text = "Date: ${task.dueDate}",
                            style = TextStyle(fontSize = 10.sp),
                            modifier = Modifier.weight(0.35f)
                        )
                        Spacer(modifier = Modifier.weight(0.10f)) // Add space between the date name and the status

                        Box(
                            modifier = Modifier
                                .weight(0.25f) // Box occupies 15% of the width
                                .fillMaxHeight()
                                .background(color = Color(0xFF04ECD1), shape = CircleShape)
                                .padding(vertical = 0.5.dp) // Adjust the vertical padding to make the box smaller
                                .aspectRatio(5f), // Set the aspect ratio to make the box square,
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = task.state, // Text to be displayed inside the rectangle
                                color = Color.Black,
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun TabListTasks(vm: TasksViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)) // Set the corner radius here
            .background(Color.LightGray) // Set the background color
    ) {
        TextField(
            value =  searchText, // Pass search text value here
            onValueChange = {
                searchText = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search...") }
        )
    }
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
                        .background(Color(0xFF026B92))
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
                Text("Team Tasks", color = if (selectedTabIndex == 0) Color.White else Color(0xFF026B92))
            }
            Tab(
                modifier = if (selectedTabIndex == 1) {
                    Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF026B92))
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
                Text("My tasks", color = if (selectedTabIndex == 1) Color.White else Color(0xFF026B92))
            }
        }

        // Content of each tab
        when (selectedTabIndex) {
//            0 -> TaskList("team", searchText, vm)
//            1 -> TaskList("myTasks", searchText, vm)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Tasks(selectTab: String,
          word:String,
          vm: TasksViewModel,
          mainview: mainViewModel,
          navController: NavController,
          mainPageNavController: NavController
) {
    mainview.setBab(true)

    mainview.setTeamTab(1)

    mainview.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
        mainview.setRightButton("", null, "") {}
        mainview.setTitle("Teams")
        mainview.setBab(false)
        mainPageNavController.popBackStack()
    }

    mainview.setRightButton("", null, ""){}

    val teamId by mainview.selectedTeamId.observeAsState()
    val tasks by mainview.getTasks(teamId!!).collectAsState(initial = listOf())

    Scaffold(
        content = {
            TaskList(tasks, word, mainview, navController)
        }
    )
}

@Composable
fun TaskList(
    tasks: List<newTask>,
    word: String,
    mainview: mainViewModel,
    navController: NavController
) {

    Column(modifier = Modifier.fillMaxSize()) {
        var taskListTab: List<newTask> = tasks
        var taskfilter: List<newTask>
//        if (selectTab == "team") taskListTab = tasks
//                else if (selectTab == "myTasks"){
//                    taskListTab = taskListTab.filter { it.assignedMembers.contains("Deodato Debbie") }.toSet()
//                    println(taskListTab)
//                }

        taskfilter = filterTasks(taskListTab, word)
        LazyColumn {
            items(tasks) { task ->
                TaskItem(task = task, navController) {
                    mainview.setActiveTask(task)
                    navController.navigate("taskDetails")
                }
            }
        }
    }
}

fun filterTasks(tasks: List<newTask>, query: String): List<newTask> {
    // Filter the list of tasks based on the search query
    return tasks.filter{task ->
        task.title.contains(query, ignoreCase = true)
                || task.category!!.contains(query, ignoreCase = true)
                || task.tag!!.contains(query, ignoreCase = true)
    }
}






