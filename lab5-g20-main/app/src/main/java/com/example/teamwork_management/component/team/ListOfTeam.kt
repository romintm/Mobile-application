package com.example.teamwork_management.component.team

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.teamwork_management.dataClasses.Team
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.Dispatcher

@Composable
fun MyTeamItem(team: newTeam,
               navController: NavController,
               vm: TeamViewModel,
               mainvm: mainViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(width = 1.dp, color = Color(0xFF0296CD), shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = {
                navController.navigate("team")
                mainvm.setSelectedTeamId(team.teamId)
                mainvm.setBab(true)

                mainvm.setTitle(team.name)
                mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                    mainvm.setBab(false)
                    navController.popBackStack()
                }

                mainvm.setRightButton("", null, content = "") {}
                mainvm.setTeamTab(1)
            })
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
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0, 69, 129), CircleShape)
                        ) {
                            AsyncImage(
                                model = team.img,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = team.name,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.85f)
                        )
                        Spacer(modifier = Modifier.weight(0.10f)) // Add space between the rectangle and the title
                        // Small rectangle
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(30.dp)
                                .background(color = Color(0xFF0296CD), shape = CircleShape)
                                .padding(vertical = 10.dp) // Adjust the vertical padding to make the box smaller
                                .aspectRatio(5f),

                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = team.category,
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp)) // Add space between the rows

                    Text(
                        text = "Date: ${team.creationdate}",
                        style = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .padding(top = 8.dp, start = 12.dp)
                            .align(Alignment.Start)
                    )
                }
            }
        }
    }
}




@Composable
fun TabListTeam(
    vm: TeamViewModel,
    navController: NavController,
    mainvm: mainViewModel,
) {
    val teams by mainvm.getTeams().collectAsState(initial = listOf())

    //mainvm.setTitle("Teams")
    mainvm.setLeftButton("", null, content = "") {}
    mainvm.setRightButton(
        "Profile",
        Icons.Default.AccountCircle,
        content = "Profile"
    ) {
        navController.navigate("profile_screen")
        mainvm.setTitle("Profile")
    }

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

    Column (
        modifier = Modifier
            .padding(top = 70.dp)
    ) {
        Spacer (modifier = Modifier.height(14.dp))
         TeamList(searchText, vm.initialTeam, navController = navController, vm, mainvm, teams)
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeamList( word:String,
              teamsStateFlow: MutableStateFlow<List<Team>>,
              navController: NavController,
              vm: TeamViewModel,
              mainvm: mainViewModel,
              teams: List<newTeam>
) {
    val teamListTab by teamsStateFlow.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mainvm.setTitle("New team")
                    navController.navigate("create_team_screen")
                },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add") // Add icon to FAB
            }
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                val teamfilter = filterTeam(teams, word)
                LazyColumn {
                    items(teamfilter) { team ->
                        MyTeamItem(team=team, navController = navController, vm, mainvm)
                    }
                }
            }
        }
    )




}


fun filterTeam(teams: List<newTeam>, query: String): List<newTeam> {
    return teams.filter{team ->
        team.name.contains(query, ignoreCase = true)
                || team.category.contains(query, ignoreCase = true)
    }
}