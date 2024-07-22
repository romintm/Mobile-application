package com.example.teamwork_management.component.userProfile

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.teamwork_management.R
import com.example.teamwork_management.viewModels.ProfileViewModel
import com.example.teamwork_management.viewModels.UserInformations
import com.example.teamwork_management.viewModels.UserViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PresentEditView(context: Context, navController: NavController, mainvm: mainViewModel, vm:ProfileViewModel= viewModel(), uvm: UserViewModel){
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

    mainvm.setLeftButton(
        "Back",
        Icons.Default.ArrowBackIosNew,
        content = "BackToProfile"
    ) {
        vm.toggleEditMode()
        navController.popBackStack()
        mainvm.setTitle("Profile")
    }

    mainvm.setRightButton("", null, "") {}

    BoxWithConstraints {
        if (this.maxHeight > maxWidth){
            Column (
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                profilePictureEdit(h = 0.3f, p = 16, context = context)
                Column {
                    userDetailsEdit(h = 0.3f)
                    //kpiEdit()
                    Spacer(modifier = Modifier.weight(1f))
                    actionButtons(navController = navController, vm = vm, mainvm= mainvm)
                }

            }
        } else {
            Row (
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (Modifier.fillMaxWidth(0.3f)){
                    profilePictureEdit(h = 1f, p = 0, context = context)
                }
                Column (Modifier.fillMaxWidth(0.5f)) {
                    userDetailsEdit(h= 1f)
                }
//                Column (Modifier.fillMaxWidth(0.8f)) {
//                    kpiEdit()
//                }
                Spacer(modifier = Modifier.weight(1f))

                Column (Modifier.fillMaxWidth(0.2f)) {
                    actionButtons(navController = navController, vm = vm, mainvm= mainvm)
                }
            }
        }
    }
}

@Composable
fun profilePictureEdit(vm: ProfileViewModel = viewModel(), h: Float, p: Int, context: Context) {
    var selectedImageUri = remember { mutableStateOf(null as Uri?) }


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            vm.setPicTaken(null)
            selectedImageUri.value= uri
            vm.userProfilePicture = uri.toString()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(h)
            .padding(start = 16.dp, end = 16.dp, top = p.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(3.dp, Color(119, 189, 223), CircleShape)
        ) {
            // Display the user profile picture if available
            when (vm.hasPicture() ){
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
                    if (selectedImageUri.value != null && selectedImageUri.value != (Uri.EMPTY)) {
                        AsyncImage(
                            model = selectedImageUri.value,
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(119, 189, 223), CircleShape),
                            contentScale = ContentScale.Crop)



                }else{
                         AsyncImage(
          model = vm.userProfilePicture,
          contentDescription = null,
          modifier = Modifier
              .size(200.dp)
              .clip(CircleShape)
              .border(3.dp, Color(119, 189, 223), CircleShape),
          contentScale = ContentScale.Crop)


                    }
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
            // Overlay a camera button on top of the profile picture
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopCenter)
            ) {
                Button(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(119, 189, 223),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(119, 189, 223),
                        disabledContentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_camera),
                        contentDescription = "Camera"
                    )
                }
                DropdownMenu(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Take a picture") },
                        onClick = {
                            vm.togglePhotoMode()
                            Toast.makeText(
                                context,
                                "User wants to take a picture",
                                Toast.LENGTH_SHORT
                            ).show()
                            expanded = false  //close the menu
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Load from gallery") },
                        onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            Toast.makeText(
                                context,
                                "User wants to laod an image from gallery",
                                Toast.LENGTH_SHORT
                            ).show()
                            expanded = false  //close the menu
                        }
                    )
                }
            }
        }
    }
}


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
fun userDetailsEdit(vm:ProfileViewModel = viewModel(), h: Float) {
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
            OutlinedTextField(
                value = vm.fullName,
                onValueChange = { newValue ->
                    // Update the ViewModel when the value changes
                    vm.setName(newValue)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ,imeAction = ImeAction.Done),
                label = { Text("Full Name")},// Optional label for the field
                isError = vm.fullNameError.isNotBlank(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(119, 189, 223))
            )
        }
        if(vm.fullNameError.isNotBlank()){
            Text(vm.fullNameError, color= MaterialTheme.colorScheme.error)
        }

//                Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            OutlinedTextField(
                value = vm.nicknameuser,
                onValueChange = { newValue ->
                    // Update the ViewModel when the value changes
                    vm.setNickName(newValue)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ,imeAction = ImeAction.Done),
                label = { Text("Nickname") }, // Optional label for the field
                isError = vm.nicknameUserError.isNotBlank(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(119, 189, 223))
            )
        }
        if(vm.nicknameUserError.isNotBlank()){
            Text(vm.nicknameUserError, color= MaterialTheme.colorScheme.error)
        }

//                Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            OutlinedTextField(
                value = vm.userEmail,
                onValueChange = { newValue ->
                    // Update the ViewModel when the value changes
                    vm.setEmail(newValue)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,imeAction = ImeAction.Done),
                label = { Text("Email") }, // Optional label for the field
                isError = vm.userEmailError.isNotBlank(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(119, 189, 223))
            )
        }
        if(vm.userEmailError.isNotBlank()){
            Text(vm.userEmailError, color= MaterialTheme.colorScheme.error)
        }

//                Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            OutlinedTextField(
                value = vm.userLocation,
                onValueChange = { newValue ->
                    // Update the ViewModel when the value changes
                    vm.setLocation(newValue)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ,imeAction = ImeAction.Done),
                label = { Text("Location") }, // Optional label for the field
                isError = vm.userLocationError.isNotBlank(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(119, 189, 223))
            )
        }
        if(vm.userLocationError.isNotBlank()){
            Text(vm.userLocationError, color= MaterialTheme.colorScheme.error)
        }

//                Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start

        ) {
            OutlinedTextField(
                value = vm.userDescriptions,
                onValueChange = { newValue ->
                    // Update the ViewModel when the value changes
                    vm.setDescriptions(newValue)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ,imeAction = ImeAction.Done),
                label = { Text("Description") }, // Optional label for the field
                isError = vm.descriptionsError.isNotBlank(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(119, 189, 223))
            )
        }
        if(vm.descriptionsError.isNotBlank()){
            Text(vm.descriptionsError, color= MaterialTheme.colorScheme.error)
        }
    }
}
}

@Composable
fun kpiEdit (vm:ProfileViewModel = viewModel()) {
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
                    "Number of Projects: " + vm.userProjects,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start

            ) {
                Text(
                    "Number of Tasks: " + vm.userTasks,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun actionButtons(navController: NavController, vm: ProfileViewModel, mainvm: mainViewModel) {
    var uvm = UserViewModel
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                // Discard changes and navigate back
                vm.toggleEditMode()
                navController.popBackStack()
                mainvm.setTitle("Profile")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text("Cancel")
        }

        Button(
            onClick = {
                // Save changes and navigate back
                // Save changes and navigate back
                if(vm.validateFields()){
                    val updatedUserInfo = UserInformations(
                        about = vm.userDescriptions,
                        city = vm.userLocation,
                        email = vm.userEmail,
                        name = vm.fullName.split(" ").firstOrNull() ?: "",
                        surname = vm.fullName.split(" ").lastOrNull() ?: "",
                        profilePicture = vm.userProfilePicture,
                        role = "",  // Add role if applicable
                        userId = uvm.user.value?.uid ?: "",
                        userProjects = vm.userProjects.toIntOrNull() ?: 0,
                        userTasks = vm.userTasks.toIntOrNull() ?: 0
                    )
                    uvm.saveUserData(updatedUserInfo)

                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300) // 0.5 seconds delay
                        vm.toggleEditMode()
                        navController.popBackStack()
                        mainvm.setTitle("Profile")
                    }
                }
            }
        ) {
            Text(text = "Save")
        }
    }
}
