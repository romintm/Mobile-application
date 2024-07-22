package com.example.teamwork_management.component.team

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisConfig
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.components.Legends
import co.yml.charts.common.extensions.getMaxElementInYAxis
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.StackedBarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import coil.compose.AsyncImage
import com.example.teamwork_management.dataClasses.Comment
import com.example.teamwork_management.dataClasses.Task
import com.example.teamwork_management.dataClasses.TaskHistoryElement
import com.example.teamwork_management.dataClasses.User
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import com.google.api.ResourceDescriptor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.min

@Composable
fun PresentTeamAchievement(context: Context, vm: TeamViewModel, mainvm: mainViewModel) {
    mainvm.setTeamTab(0)
    val selectedTeamId by mainvm.selectedTeamId.observeAsState(0)
    val selectedTeam by mainvm.getSelectedTeam(selectedTeamId).collectAsState(initial = newTeam())

    // Collect states outside of the loop
    val members by mainvm.getMembers(selectedTeamId).collectAsState(initial = listOf())
    val tasks by mainvm.getTasks(selectedTeamId).collectAsState(initial = listOf())

    var users by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(members) {
        users = members.map { member ->
            val memberTeams = mainvm.getUsersTeams(member.userId).firstOrNull() ?: 0
            User(
                id = member.userId,
                _name = member.name,
                _surname = member.surname,
                _profilePicture = member.profilePicture,
                _email = member.email,
                _city = member.city,
                _about = member.about,
                _userTeams = memberTeams,
                _userProjects = member.userProjects,
                _userTasks = member.userTasks,
                _role = member.role
            )
        }
    }

    var taskList by remember { mutableStateOf(listOf<Task>()) }

    LaunchedEffect(tasks) {
        val taskListResult = tasks.map { task ->
            val comments: MutableList<Comment> = mutableListOf()
            val histories: MutableList<TaskHistoryElement> = mutableListOf()

            val dueDate = parseDate(task.dueDate!!) ?: Date()

            Task(
                id = task.taskId,
                _title = task.title,
                _description = task.description,
                _tag = task.tag,
                _category = task.category,
                _assignedMembers = task.assignedMembers.toMutableList(),
                _dueDate = dueDate,
                _state = task.state,
                _comments = comments,
                _history = histories
            )
        }.toMutableList()
        taskList = taskListResult
    }

    BoxWithConstraints {
        if (this.maxHeight > maxWidth) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                teamAchievements(vm = vm, tasks = taskList)
                Spacer(modifier = Modifier.height(16.dp))
                individualAchievements(vm = vm, members = users, tasks = taskList, mainvm = mainvm)
            }
        } else {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f).fillMaxWidth()) {
                    teamAchievements(vm = vm, tasks = taskList)
                }
                Column(
                    Modifier.weight(1f).fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    individualAchievements(vm = vm, members = users, tasks = taskList, mainvm = mainvm)
                }
            }
        }
    }
}


@Composable
fun teamAchievements(vm: TeamViewModel, tasks: List<Task>){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ){
        Text(
            "Team achievements",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0, 69, 129)
        )
        Spacer(modifier = Modifier.height(16.dp))
        barChar(vm, tasks)
    }
}

@Composable
fun barChar(vm: TeamViewModel, tasks : List<Task>){

    // Group tasks by week
    val tasksByWeek = tasks.groupBy { task ->
        val calendar = Calendar.getInstance()
        calendar.time = task.dueDate
        calendar[Calendar.WEEK_OF_YEAR]
    }

    // Count completed tasks for each week
    val completedTasksByWeek = tasksByWeek.mapValues { (_, tasks) ->
        tasks.count { it.state == "Completed" }
    }

    var maxRange = completedTasksByWeek.values.maxOfOrNull { it } ?: 5
    if(maxRange < 5){
        maxRange = 5
    }
    val yStepSize = 5   // Number of steps displayed

    // Determine current week
    val currentWeek = Calendar.getInstance()[Calendar.WEEK_OF_YEAR]

    // Create BarData instances for each week, including empty weeks up to today
    val barData = mutableListOf<BarData>()
    val earliestWeek = tasksByWeek.keys.minOrNull() ?: currentWeek
    var index = 1
    for (week in earliestWeek..currentWeek) {
        val completedTasks = completedTasksByWeek[week] ?: 0
        barData.add(BarData(Point(week.toFloat(), completedTasks.toFloat()), color = Color(0, 69, 129), label = "Week $index"))
        index++
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(12.dp)
        .axisLabelAngle(20f)
        .startDrawPadding(48.dp)
        .shouldDrawAxisLineTillEnd(true)
        .labelData { index -> barData[index].label }
        .build()
    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()
    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            paddingBetweenBars = 15.dp,
            barWidth = 40.dp,
            selectionHighlightData = SelectionHighlightData(
                isHighlightFullBar = true,
                popUpLabel = { name, value ->
                    " Completed tasks: ${value.toInt()} "
                },
                highlightTextBackgroundColor = Color.Black,
                highlightTextColor = Color.White
            )
        ),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 10.dp,
        drawBar = { drawScope, barData, drawOffset, height, barChartType, barStyle ->
            with(drawScope) {
                with(barStyle) {
                    drawRect(
                        color = barData.color,
                        topLeft = drawOffset,
                        size = if (barChartType == BarChartType.VERTICAL) Size(
                            barWidth.toPx(),
                            height
                        ) else Size(height, barWidth.toPx()),
                        style = barDrawStyle,
                        blendMode = barBlendMode
                    )
                }
            }
        }
    )
    BarChart(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp), barChartData = barChartData)
}

@Composable
fun individualAchievements(vm: TeamViewModel, members : List<User>, tasks : List<Task>, mainvm: mainViewModel) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ){
        Text(
            "Individual completed tasks",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0, 69, 129)
        )
        Spacer(modifier = Modifier.height(16.dp))
        userList(vm, members, tasks, mainvm)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun userList(vm: TeamViewModel, members : List<User>, tasks : List<Task>, mainvm: mainViewModel) {

    // Initialize a map to store the counts for each user
    val userTasksCount = mutableMapOf<String, Int>()
    val userCompletedTasksCount = mutableMapOf<String, Int>()

    // Iterate through each task
    for (task in tasks) {
        val members by mainvm.getTaskMembers(task.id).collectAsState(initial = listOf())
        // Iterate through each assigned member of the task

        for (member in members) {
            // Increment the total tasks count for the member
            userTasksCount[member.name + " " + member.surname] = (userTasksCount[member.name + " " + member.surname] ?: 0) + 1

            // If the task is completed, increment the completed tasks count for the member
            if (task.state == "Completed") {
                userCompletedTasksCount[member.name + " " + member.surname] = (userCompletedTasksCount[member.name + " " + member.surname] ?: 0) + 1
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()){
        LazyColumn(
            modifier = Modifier
                .height(400.dp)
        ) {
            items(members) { user ->
                // Calculate the completed tasks count and total tasks count for the user
                val completedTasksCount = userCompletedTasksCount[user.name + " " + user.surname] ?: 0
                val totalTasksCount = userTasksCount[user.name + " " + user.surname] ?: 0

                // Construct the string representing completed tasks over total tasks
                val completionRatio = "$completedTasksCount/$totalTasksCount"

                // Pass the completion ratio string to the UserListItem
                UserListItem(user, completionRatio)
            }
        }
    }
}

@Composable
fun UserListItem(user: User, ratio: String) {
    Box{
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(5f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    ProfileImage(imageUrl = user.profilePicture)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = user.name + " " + user.surname,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = ratio,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                    textAlign =  TextAlign.End
                )
            }

        }
        // Add a bottom border below the row
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}


@Composable
fun ProfileImage(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .border(3.dp, Color(119, 189, 223), CircleShape),
        contentScale = ContentScale.Crop)
}

/**
 * Function to parse a date string based on two possible formats: "dd/MM/yyyy" or "yyyy-MM-dd".
 * Returns null if parsing fails.
 */
private fun parseDate(dateString: String): Date? {
    val pattern1 = "dd/MM/yyyy"
    val pattern2 = "yyyy-MM-dd"

    // Attempt to parse using pattern1
    try {
        return SimpleDateFormat(pattern1, Locale.getDefault()).parse(dateString)
    } catch (e1: ParseException) {
        // If parsing fails with pattern1, try pattern2
        try {
            return SimpleDateFormat(pattern2, Locale.getDefault()).parse(dateString)
        } catch (e2: ParseException) {
            // If parsing fails with both patterns, return null
            e2.printStackTrace()
            return null
        }
    }
}
