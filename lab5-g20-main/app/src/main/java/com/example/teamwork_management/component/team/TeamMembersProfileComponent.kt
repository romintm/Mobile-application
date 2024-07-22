package com.example.teamwork_management.component.team

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.teamwork_management.component.userProfile.kpi
import com.example.teamwork_management.component.userProfile.profilePicture
import com.example.teamwork_management.component.userProfile.userDetails
import com.example.teamwork_management.dataClasses.User
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.models.mainModel
import com.example.teamwork_management.viewModels.ProfileViewModel
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.mainViewModel

@Composable
fun TeamMemberProfile(vm: TeamViewModel, mainview: mainViewModel, navController : NavController) {
    val user by mainview.selectedUser.observeAsState()
    val userTeams by mainview.getUsersTeams(user!!.userId).collectAsState(initial = 0)

    mainview.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
        mainview.setBab(true)
        navController.popBackStack()
        mainview.setTitle(mainview.selectedTeam.value!!.name)
    }

    BoxWithConstraints {
        if (this.maxHeight > maxWidth){
            Column (
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TeamMemberPicture(h = 0.3f, p = 16, user = user!!)
                Column {
                    TeamMemberDetails(h = 0.3f, user = user!!)
                    TeamMemberDetailsKPI(user = user!!, userTeams = userTeams)
                }

            }
        } else {
            Row (
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (Modifier.fillMaxWidth(0.3f)){
                    TeamMemberPicture(h = 1f, p = 0, user = user!!)
                }
                Column (Modifier.fillMaxWidth(0.5f)) {
                    TeamMemberDetails(h= 1f, user = user!!)
                }
                Column (Modifier.fillMaxWidth(0.8f)) {
                    TeamMemberDetailsKPI(user = user!!, userTeams = userTeams)
                }
            }
        }
    }
}

@Composable
fun TeamMemberPicture (vm: ProfileViewModel = viewModel(),
                       h: Float,
                       p: Int,
                       user: newUser
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(h)
            .padding(start = 16.dp, end = 16.dp, top = p.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    )
    {
        when (vm.hasPicture()){
            0 ->{
                Column (
                    modifier = Modifier
                        .border(2.dp, Color(119, 189, 223), CircleShape)
                        .size(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = vm.getMonogram(),
                        style = TextStyle(fontSize = 36.sp),
                    )

                }}
            1-> {
                AsyncImage(
                    model = user.profilePicture,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(119, 189, 223), CircleShape),
                    contentScale = ContentScale.Crop)

            }
            2->
            {
                vm.userProfilePicture = ""
                Image(bitmap = vm.picTaken!!.asImageBitmap()
                    , contentDescription = "profile Picture",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(119, 189, 223), CircleShape),)
            }
        }}

}

@Composable
fun TeamMemberDetails(vm: ProfileViewModel = viewModel(), h: Float, user: newUser) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ){
        Column(modifier = Modifier.fillMaxSize()){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Personal Information:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            )
            {
                Text(
                    user.name,
                    modifier = Modifier
                        .weight(1f)
                )
            }

//                Spacer(modifier = Modifier.size(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    user.surname,
                    modifier = Modifier
                        .weight(1f)
                )
            }

//                Spacer(modifier = Modifier.size(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    user.email,
                    modifier = Modifier
                        .weight(1f)
                )
            }

//                Spacer(modifier = Modifier.size(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    user.city,
                    modifier = Modifier
                        .weight(1f)
                )
            }

//                Spacer(modifier = Modifier.size(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start

            ) {
                Text(
                    user.about,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun TeamMemberDetailsKPI (vm:ProfileViewModel = viewModel(), user: newUser, userTeams: Int) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ){
        Column(modifier = Modifier.fillMaxSize()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "KPI:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Number of Teams: " + userTeams,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Number of Projects: " + user.userProjects,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start

            ) {
                Text(
                    "Number of Tasks: " + user.userTasks,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}