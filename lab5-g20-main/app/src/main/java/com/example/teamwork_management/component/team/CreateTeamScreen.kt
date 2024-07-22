package com.example.teamwork_management.component.team

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.teamwork_management.dataClasses.Task
import com.example.teamwork_management.dataClasses.User
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CreateTeamScreen(vm:TeamViewModel, navController: NavController, mainvm: mainViewModel){
    mainvm.setRightButton("", null, content = ""){}
    var title by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf("") }

    var description by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf("") }

    var category by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var members by remember { mutableStateOf(mutableListOf(User("1","","","","","","",0,0,0,""))) }
    var task by remember { mutableStateOf(mutableListOf( Task(0, "", "", "", "", mutableListOf(""), null, "", null, null))) }
    val currentDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    }

    fun checkTitle() {
        if (title.isBlank()) {
            titleError = "Title Can not be empty"
        } else {
            titleError = ""
        }
    }
    fun checkDes() {
        if (description.isBlank()) {
            descriptionError = "Description Can not be empty"
        } else {
            descriptionError = ""
        }
    }
    fun checkCat() {
        if (category.isBlank()) {
            categoryError = "Category Can not be empty"
        } else {
            categoryError = ""
        }
    }

    Column(
        modifier = Modifier

            .fillMaxHeight()
            .background(Color(255, 255, 255))
            .verticalScroll(ScrollState(1)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

     Box(
            modifier = Modifier
                .background(Color(119, 189, 223))
                .fillMaxWidth()
                .height(200.dp)

        ) {
         Row(modifier = Modifier.fillMaxWidth()) {

             // Left part of the box
             Column(
                 modifier = Modifier
                     .weight(1f),
                 horizontalAlignment = Alignment.Start,
                 verticalArrangement = Arrangement.Center
             ) {
                 ImageWithButton { uri -> selectedImageUri = uri }
             }

             // Right part of the box
             Column(
                 modifier = Modifier
                     .weight(1f)
                     .padding(end = 16.dp, top = 80.dp, start = 15.dp),
                 horizontalAlignment = Alignment.End,
                 verticalArrangement = Arrangement.Center
             ) {
                 Text(
                     text = "Select Your Team Profile",
                     fontWeight = FontWeight.Bold,
                     fontSize = 25.sp


                 )
                 Icons.Default.BrowseGallery
             }
         }
     }
        Spacer(modifier = Modifier.height(24.dp))


        //Name of the Team
        Text(
            text = "Team Title",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, end = 16.dp),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Color(232, 232, 232), shape = RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(246, 246, 246),
                    unfocusedPlaceholderColor = Color(155, 155, 155),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(246, 246, 246),
                    focusedPlaceholderColor = Color(199, 199, 199),
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    errorPlaceholderColor = Color(155, 155, 155),
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Team Title") },
                singleLine = true,
                isError = titleError.isNotBlank()
            )
        }
        if (titleError.isNotBlank()) {
            Text(
                text = titleError,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp, end = 16.dp),
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Description of the Team
        Text(
            text = "Team Description",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, end = 16.dp),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                value = description,
                onValueChange = { description = it },
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
                placeholder = { Text("Team Description") },
                maxLines = 10
            )
        }
        if (descriptionError.isNotBlank()) {
        Text(
            text = descriptionError,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp
        )
    }

        Spacer(modifier = Modifier.height(16.dp))



        //Category of the task
        Text(
            text = "Team Category",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, end = 16.dp),
            color = Color(151, 203, 220)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                value = category,
                onValueChange = { category = it },
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
                placeholder = { Text("Team Category") },
                singleLine = true
            )
        }
        if (categoryError.isNotBlank()) {
        Text(
            text = categoryError,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp
        )
    }



        Spacer(modifier = Modifier.height(26.dp))

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    mainvm.setTitle("Teams")
                    navController.navigate("home")
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
                    checkDes()
                    checkCat()
                    if (titleError.isBlank() && categoryError.isBlank() && descriptionError.isBlank()){
                        val profilePictureUri = selectedImageUri ?: Uri.parse("https://st3.depositphotos.com/3591429/15783/i/450/depositphotos_157835190-stock-photo-business-people-holding-hands-together.jpg")
                        /*vm.newTeam(
                            title=title,
                            img= profilePictureUri.toString(),
                            description=description,
                            category=category,
                            creationdate = currentDate,
                            members = members,
                            tasks = task,

                            )*/
                        val newteam = newTeam(
                            name = title,
                            img = profilePictureUri.toString(),
                            description = description,
                            category = category,
                            creationdate = currentDate
                        )
                        mainvm.createTeam(newteam)
                        mainvm.setTitle("Teams")
                        navController.navigate("home")

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
                Text(text = "Create")
            }
        }
    }
}

@Composable
fun ImageWithButton(onImageSelected: (Uri?) -> Unit) {
    var selectedImageUri = remember { mutableStateOf(null as Uri?) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
            onImageSelected(uri)
        }
    )
    val img = remember(selectedImageUri) {
        derivedStateOf {
            selectedImageUri.value?.toString()?:"https://st3.depositphotos.com/3591429/15783/i/450/depositphotos_157835190-stock-photo-business-people-holding-hands-together.jpg"
        }}
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 20.dp, bottom = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(5.dp, Color(0, 69, 129), CircleShape)
        ) {

            AsyncImage(
                model = img.value,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 4.dp)

        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )

        }
    }
}



