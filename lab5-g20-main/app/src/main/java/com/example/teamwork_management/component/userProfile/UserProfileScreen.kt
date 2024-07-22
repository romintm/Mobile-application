package com.example.teamwork_management.component.userProfile

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.teamwork_management.component.signin.GoogleAuthUiClient
import com.example.teamwork_management.viewModels.ProfileViewModel
import com.example.teamwork_management.viewModels.UserViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import kotlinx.coroutines.launch


// show the user Info
@Composable
fun PresentProfileView(context: Context, navController: NavController, mainvm: mainViewModel, vm: ProfileViewModel =viewModel(), uvm: UserViewModel, onSignOutClick: () -> Unit){
    val user by uvm.userInformation.collectAsState()

    user?.let { userInfo ->
        vm.setName("${userInfo.name} ${userInfo.surname}")
        vm.setDescriptions(userInfo.about)
        vm.setLocation(userInfo.city)
        vm.setEmail(userInfo.email)
        vm.setProfilePicture(userInfo.profilePicture)
        vm.setuserProjects(userInfo.userProjects.toString())
        vm.setuserTasks(userInfo.userTasks.toString())
        vm.setNickName(userInfo.name)
    } ?: run {
        vm.setName("")
        vm.setDescriptions("")
        vm.setLocation("")
        vm.setEmail("")
        vm.setProfilePicture("")
        vm.setuserProjects("")
        vm.setuserTasks("")
        vm.setNickName("")
    }


    mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
        navController.popBackStack()
        mainvm.setTitle("Teams")
    }

    mainvm.setRightButton("Edit", Icons.Default.Edit, content = "Edit profile") {
        navController.navigate("edit_profile_screen")
        mainvm.setTitle("Edit profile")
    }

        BoxWithConstraints {
            if (this.maxHeight > maxWidth){
                Column (
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    profilePicture(h = 0.3f, p = 16, vm = vm)
                    Column {
                        userDetails(h = 0.3f)
                        kpi(onSignOutClick = onSignOutClick)
                    }

                }
            } else {
                Row (
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (Modifier.fillMaxWidth(0.3f)){
                        profilePicture(h = 1f, p = 0, vm = vm)
                    }
                    Column (Modifier.fillMaxWidth(0.5f)) {
                        userDetails(h= 1f)
                    }
                    Column (Modifier.fillMaxWidth(0.8f)) {
                        kpi(onSignOutClick =  onSignOutClick)
                    }
                }
            }
        }
    }

    @Composable
    fun profilePicture (vm:ProfileViewModel, h: Float, p: Int) {
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
                   model = vm.userProfilePicture,
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
            }
        }
    }

    @Composable
    fun userDetails(vm:ProfileViewModel = viewModel(), h: Float) {
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
                        vm.fullName,
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
                        vm.nicknameuser,
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
                        vm.userEmail,
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
                        vm.userLocation,
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
                        vm.userDescriptions,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }

@Composable
fun kpi(vm: ProfileViewModel = viewModel(), onSignOutClick: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                    "Number of Projects: " + vm.userProjects,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Number of Tasks: " + vm.userTasks,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // optional, to add some bottom padding
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onSignOutClick) {
                    Text(text = "Sign out")
                }
            }
        }
    }
}




