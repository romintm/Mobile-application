package com.example.teamwork_management.component.team

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.teamwork_management.component.userProfile.kpi
import com.example.teamwork_management.component.userProfile.profilePicture
import com.example.teamwork_management.component.userProfile.userDetails
import com.example.teamwork_management.dataClasses.Team
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.viewModels.ProfileViewModel
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.UserInformations
import com.example.teamwork_management.viewModels.UserViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@Composable
fun PresentJoinTeam(context: Context, navController: NavController, mainvm: mainViewModel, vm: TeamViewModel, teamId: Int){
    val uvm = UserViewModel
    val user by uvm.userInformation.collectAsState()

    Log.e("USER DATA", user?.userId ?: "null")

    val team by mainvm.getSelectedTeam(teamId).collectAsState(initial = newTeam())

    mainvm.setLeftButton(
        "Back",
        Icons.Default.ArrowBackIosNew,
        content = "BackToHome"
    ) {
        navController.navigate("home")
    }

    mainvm.setTitle("Join team")
    BoxWithConstraints {
        if (this.maxHeight > maxWidth){
            Column (
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                title()
                teamPicture(team = team)
                buttons(navController, mainvm, user?.userId ?: "TFmVxCQ2CUZBnFfxGF2fHuQyKSY2", teamId)
            }
        } else {
            Row (
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                title()
                teamPicture(team = team)
                buttons(navController, mainvm, user?.userId ?: "TFmVxCQ2CUZBnFfxGF2fHuQyKSY2", teamId)
            }
        }
    }
}

@Composable
fun title(){
    Text(
        text = "You have been invited to join a team",
        fontWeight = FontWeight.Bold,
        color = Color(119, 189, 223),
        fontSize = 30.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp)
        )

    Spacer(modifier = Modifier.size(32.dp))
}

@Composable
fun teamPicture(team: newTeam){
    AsyncImage(
        model = team.img,
        contentDescription = null,
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .border(3.dp, Color(119, 189, 223), CircleShape),
        contentScale = ContentScale.Crop)

    Spacer(modifier = Modifier.size(32.dp))
}

@Composable
fun buttons(navController: NavController, mainvm: mainViewModel, userId: String, teamId: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                navController.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            androidx.compose.material3.Text("Cancel")
        }

        Button(
            onClick = {
                mainvm.assignUserToTeam(teamId = teamId, userId = userId)
                navController.navigate("home")
            }
        ) {
            androidx.compose.material3.Text(text = "Join")
        }
    }
}
