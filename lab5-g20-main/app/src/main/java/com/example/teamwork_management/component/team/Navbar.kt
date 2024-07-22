package com.example.teamwork_management.component.team

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teamwork_management.dataClasses.NavbarInfo
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.mainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navbar (vm: mainViewModel,
            leftButton: NavbarInfo,
            rightButton: NavbarInfo,
){
    val c = Color(red = 119, green = 189, blue = 223)
    val navbarColor = Color(255,255,255)
    val navbarTextColor = Color(0,69,129)
    val title by vm.navbarTitle.collectAsState()
    val teamId by vm.selectedTeamId.observeAsState()
    if (teamId!! > 0) {
        val b by vm.getSelectedTeam(vm.selectedTeamId.value!!).collectAsState(initial = newTeam())
        //vm.setTitle(b.name)
    }


    Column(modifier = Modifier
        .fillMaxWidth()
        .height(75.dp)
        .background(Color(255, 255, 255)),
        verticalArrangement = Arrangement.Center
    )
    {
        CenterAlignedTopAppBar(
            title = {
                Text(text = title, fontSize = 28.sp, color = navbarTextColor)
            },
            colors = TopAppBarDefaults.topAppBarColors(navbarColor),
            actions = {
                println(vm.rightButton.value)
                if (vm.rightButton.value.title.isNotBlank()){
                    println(vm.rightButton.value)
                    IconButton(onClick = vm.rightButton.value.bfun ) {
                        Icon(imageVector = vm.rightButton.value.icon!!, contentDescription = vm.rightButton.value.content)
                    }
                }
            },
            navigationIcon = {
                println(vm.leftButton.value)
                if (vm.leftButton.value.title.isNotBlank()){
                    println(vm.leftButton.value)
                    IconButton(onClick = vm.leftButton.value.bfun ) {
                        Icon(imageVector = vm.leftButton.value.icon!!, contentDescription = vm.leftButton.value.content)
                    }
                }
            }
        )
    }
}