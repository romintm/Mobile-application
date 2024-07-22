package com.example.teamwork_management.component.team


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.BarChart
import androidx.compose.material.icons.sharp.Chat
import androidx.compose.material.icons.sharp.Groups
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material.icons.sharp.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import coil.compose.AsyncImage
import com.example.teamwork_management.component.CreateNewTask
import com.example.teamwork_management.component.EditTaskScreen
import com.example.teamwork_management.component.Tasks
import com.example.teamwork_management.component.chats.ChatListScreen
import com.example.teamwork_management.component.chats.ChatScreen
import com.example.teamwork_management.component.tasks.PresentTaskDetailView
import com.example.teamwork_management.dataClasses.newTeam
import com.example.teamwork_management.dataClasses.newUser
import com.example.teamwork_management.models.ChatListViewModel
import com.example.teamwork_management.viewModels.ChatViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import com.example.teamwork_management.viewModels.TasksViewModel
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.UserViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter


val inviteLink = "https://www.teamworkmanagement.com/"

fun buildAnnotatedLink(teamId: String): AnnotatedString {
    val inviteLink = "$inviteLink$teamId"

    return buildAnnotatedString {
        // We attach this *URL* annotation to the following content
        // until `pop()` is called
        pushStringAnnotation(
            tag = "URL", annotation = inviteLink
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue, fontWeight = FontWeight.Bold
            )
        ) {
            append(inviteLink)
        }

        pop()
    }
}

@Composable
fun ShowTeamDetailsPage(mainvm: mainViewModel,
                        atvm: TeamViewModel,
                        clvm: ChatListViewModel,
                        mainPageNavController: NavController,
                        uvm: UserViewModel,
                        context: Context
) {

    //This will be used by all the tabs
    mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
        println("Kir to kose nanat Malnati")
        mainvm.setRightButton("Profile", Icons.Default.AccountCircle, content = "Profile") {
            mainPageNavController.navigate("profile_screen")
        }
        mainvm.setBab(false)
        mainPageNavController.popBackStack()
        mainvm.setTitle("Teams")
    }



    mainvm.setRightButton("", null, "") {}

    val navController = rememberNavController()
    val uri = "https://www.teamworkmanagement.com"
    val qrCodeBitmap = generateQRCode(inviteLink)

    val selectedTeamId by mainvm.selectedTeamId.observeAsState(0)
    val selectedTeam by mainvm.getSelectedTeam(selectedTeamId).collectAsState(initial = newTeam())

    LaunchedEffect(selectedTeam) {
        mainvm.setTeam(selectedTeam)
        atvm.setTeam(selectedTeam)
    }

    Scaffold (
        modifier = Modifier
            .background(Color(255, 255, 255)),
        bottomBar = {
            if (mainvm.bab.value!!) {
                BottomAppBar(actions = {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            navController.navigate("myteam")

                            mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                mainvm.setBab(false)
                                mainPageNavController.popBackStack()
                                mainvm.setTitle("Teams")
                            }
                            mainvm.setRightButton("", null, "") {}
                            mainvm.setTeamTab(1) }
                        ) {
                            Icon(imageVector = Icons.Sharp.List, contentDescription = "ListOfTasks")
                        }

                        IconButton(onClick = {
                            navController.navigate("teamMembers")

                            mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                mainvm.setBab(false)
                                mainPageNavController.popBackStack()
                                mainvm.setTitle("Teams")
                            }
                            mainvm.setRightButton("", null, "") {}
                            mainvm.setTeamTab(2) }
                        ) {
                            Icon(imageVector = Icons.Sharp.Groups, contentDescription = "TeamMembers")
                        }

                        IconButton(onClick = {
                            navController.navigate("achievement")

                            mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                mainvm.setBab(false)
                                mainPageNavController.popBackStack()
                                mainvm.setTitle("Teams")
                            }
                            mainvm.setRightButton("", null, "") {}
                            mainvm.setTeamTab(3) }
                        ) {
                            Icon(imageVector = Icons.Sharp.BarChart, contentDescription = "Performances")
                        }

                        IconButton(onClick = {
                            navController.navigate("chats")

                            mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                mainvm.setBab(false)
                                mainPageNavController.popBackStack()
                                mainvm.setTitle("Teams")
                            }
                            mainvm.setRightButton("", null, "") {}
                            mainvm.setTeamTab(4) }
                        ) {
                            Icon(imageVector = Icons.Sharp.Chat, contentDescription = "Chats")
                        }

                        IconButton(onClick = {
                            navController.navigate("teamInfo")

                            mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                mainvm.setBab(false)
                                mainPageNavController.popBackStack()
                                mainvm.setTitle("Teams")
                            }
                            mainvm.setRightButton("Edit", Icons.Default.Edit, content = "EditTeam") {
                                mainvm.setBab(false)
                                navController.navigate("editInfo")
                                mainvm.setTitle("Edit " + selectedTeam.name)
                                mainvm.setRightButton("", null, "") {}
                                mainvm.setLeftButton("", null, "") {}
                            }

                            mainvm.setTeamTab(5)
                        }) {
                            Icon(imageVector = Icons.Sharp.Info, contentDescription = "TeamManagement")
                        }
                    }
                })
            }
        },
        floatingActionButton = {
            if (mainvm.teamTab.value == 2){
                FloatingActionButton(onClick = { navController.navigate("invite") },
                    modifier = Modifier
                        .border(1.dp, Color.Black, RoundedCornerShape(15.dp)),
                    containerColor = Color(255, 255, 255),
                    shape = RoundedCornerShape(15.dp)
                    ){
                        Icon(imageVector = Icons.Sharp.Add,
                            contentDescription = "AddMember",
                            )
                    }
            } else if (mainvm.teamTab.value == 1) {
                FloatingActionButton(onClick = {
                    mainvm.setBab(false)
                    //mainvm.setLeftButton("", null, "") {}
                    mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                        mainvm.setRightButton("", null, "") {}
                        mainvm.setBab(false)
                        mainvm.setTeamTab(1)
                        mainvm.setTitle(mainvm.selectedTeam.value!!.name)
                        navController.popBackStack()
                        mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                            mainvm.setRightButton("", null, "") {}
                            mainvm.setTitle("Teams")
                            mainvm.setBab(false)
                            mainPageNavController.popBackStack()
                        }
                    }
                    mainvm.setTitle("New Task")
                    mainvm.setTeamTab(0)
                    navController.navigate("newTask")
                    },
                    modifier = Modifier
                        .border(1.dp, Color.Black, RoundedCornerShape(15.dp)),
                    containerColor = Color(255, 255, 255),
                    shape = RoundedCornerShape(15.dp)
                ){
                    Icon(imageVector = Icons.Sharp.Add,
                        contentDescription = "NewTask",
                    )
                }
            }
        }
    ) {
        it ->

        NavHost(navController = navController, startDestination = "myteam", modifier = Modifier.padding(it)){
            composable("myteam", deepLinks = listOf(navDeepLink { uriPattern = "$uri/myteam" })){
                Tasks(selectTab = "team", word = "", vm = TasksViewModel(), mainview = mainvm, navController = navController, mainPageNavController = mainPageNavController)
            }
            composable("teamMembers"){
                Members(vm = atvm, mainvm = mainvm, navController =  navController, mainPageNavController = mainPageNavController)
            }
            composable("achievement"){
                PresentTeamAchievement(LocalContext.current.applicationContext, TeamViewModel(), mainvm)
            }
            dialog("invite"){
                InviteDialog(mainvm, navController, qrCodeBitmap, context)
            }
            composable("teamInfo"){
                TeamDetails(atvm, mainvm, navController, mainPageNavController)
            }
            composable("editInfo"){
                EditTeamScreen(context = null,
                    vm = atvm,
                    navController = navController,
                    mainvm = mainvm,
                    mainPageNavController = mainPageNavController
                )
            }
            composable("chats"){
                ChatListScreen(navController =  navController,
                    vm = clvm,
                    mainPageNavController = mainPageNavController,
                    mainvm = mainvm,
                    uvm =  uvm )
            }
            composable("chat/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
                val chatListViewModel: ChatListViewModel = viewModel()
                val chatViewModel: ChatViewModel = viewModel()
                ChatScreen(chatId = chatId,
                    clvm = chatListViewModel,
                    vm = chatViewModel,
                    mainvm = mainvm)
            }

            composable("newTask") {
                CreateNewTask(vm = mainvm, navController = navController, mainPageNavController = mainPageNavController)
            }

            composable("taskDetails"){
                PresentTaskDetailView(vm = mainvm, navController = navController)
            }

            composable("editTask") {
                EditTaskScreen(mainvm = mainvm, navController = navController, mainPageNavController = mainPageNavController)
            }
        }
    }
}

@Composable
fun Members(
    vm: TeamViewModel,
    mainvm: mainViewModel,
    navController: NavController,
    mainPageNavController: NavController
) {
    mainvm.setTeamTab(2)
    val members by mainvm.getMembers(mainvm.selectedTeamId.value!!).collectAsState(initial = listOf())
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TeamMembersComponent(vm = vm,
            mainvm = mainvm,
            navController = navController,
            mainPageNavController = mainPageNavController,
            members = members)
    }
}

@Composable
fun TeamMembersComponent(vm: TeamViewModel,
                         mainvm: mainViewModel,
                         navController: NavController,
                         mainPageNavController: NavController,
                         clvm: ChatListViewModel = viewModel(),
                         members: List<newUser>
) {
    val newChatId by clvm.newChatId.collectAsState()

    LaunchedEffect(newChatId) {
        newChatId?.let {
            //println("Chat returned from creation chat: ${newChatId} ")
            navController.navigate("chat/$it")
            clvm.resetNewChatId() // Reset newChatId after navigation
    }
    }


//    println("------------- Team members -------------------------")
//    println("------- current user image: ${mainvm.loggedinUser.currentUser?.photoUrl}")
//    println("------- current user name: ${mainvm.loggedinUser.currentUser?.displayName}")
        val currentUsername = mainvm.loggedinUser.currentUser?.displayName?.split(" ")?.firstOrNull()
    //println("------- current user name first: ${currentUsername}")


        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
            members.forEachIndexed {
                index, member ->
                //println("------ member: ${member}")

                item {
                    Column(
                        modifier = Modifier
                            .height(250.dp)
                            .padding(5.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row (
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            AsyncImage(model = member.profilePicture,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(3.dp, Color(119, 189, 223), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Row (
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(text = "${member.name} ${member.surname}", fontWeight = FontWeight.Bold)
                        }

                        Row (
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(text = member.role!!, fontSize = 12.sp)
                        }
                        Row {
                            Box (
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
                            ){
                                Row (
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(onClick = {
                                        val nameChat = "${currentUsername} - ${member.name}"
                                        val chatId = mainvm.loggedinUser.uid?.let {
                                            member.profilePicture?.let { it1 ->
                                                clvm.createNewChat(
                                                    it,
                                                    member.userId,
                                                    nameChat,
                                                    mainvm.loggedinUser.currentUser?.photoUrl.toString(),
                                                    it1
                                                )
                                            }
                                        }
                                        //println("Chat returned from creation chat: ${newChatId} ")
                                        navController.navigate("chat/${newChatId}")
                                        mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                            mainvm.setBab(true)
                                            navController.navigate("teamMembers")
                                            mainvm.setTeamTab(2)
                                            mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                                mainvm.setBab(false)
                                                mainPageNavController.popBackStack()
                                                mainvm.setTitle("Teams")
                                            }
                                        }
                                        mainvm.setTeamTab(0)
                                                     },
                                        modifier = Modifier
                                            .fillMaxWidth(0.48f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = mainvm.buttonContainerColor
                                        ),
                                        enabled = member.userId != mainvm.loggedinUser.uid // Disable if the member is the logged-in user
                                    ) {
                                        Text(text = "Message", fontSize = 9.sp)
                                    }
                                    Button(onClick = {
                                        mainvm.setUser(member)
                                        mainvm.setTitle("User Profile")
                                        mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToMembersList") {
                                            mainPageNavController.popBackStack()
                                            mainvm.setTitle(mainvm.selectedTeam.value!!.name)
                                            mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
                                                mainPageNavController.popBackStack()
                                                mainvm.setLeftButton("Home", Icons.Default.Home, content = "Home") {}
                                                mainvm.setTitle("Teams")
                                            }
                                        }
                                        mainPageNavController.navigate("memberProfile")
                                    },
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = mainvm.buttonContainerColor
                                        )
                                    ) {
                                        Text(text = "Profile", fontSize = 9.sp)
                                    }
                                }
                            }
                        }
                        Row (
                            modifier = Modifier
                                .padding(5.dp)
                        ){
                            Button(onClick = {
                                mainvm.removeUserFromTeam(member.userId, mainvm.selectedTeamId.value!!)
                            },
                                modifier = Modifier
                                    .fillMaxWidth(0.5f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text(text = "Remove", fontSize = 9.sp)
                            }
                        }
                    }

                }
            }
        }
}

@Composable
fun InviteDialog (vm: mainViewModel, navController: NavController, qrCodeBitmap: Bitmap, context: Context) {

    Dialog(onDismissRequest = { navController.navigate("teamMembers") },
    ) {
        Column (
            modifier = Modifier
                .background(Color(151, 203, 220), RoundedCornerShape(5.dp))
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.5f)
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row (
                modifier = Modifier
                    .padding(top = 8.dp)
            ){
                Image(
                    bitmap = qrCodeBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    contentScale = ContentScale.Fit
                )
            }
            Row (
                modifier = Modifier
                    .padding(top = 8.dp)
            ){
                ClickableText(text = buildAnnotatedLink(vm.selectedTeam.value!!.teamId.toString()),
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {

                }
            }
            Row (
                modifier = Modifier
                    .padding(top = 8.dp)
            ){
                Button(onClick = {
                    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Invite Link", buildAnnotatedLink(vm.selectedTeam.value!!.teamId.toString()).text)
                    clipboardManager.setPrimaryClip(clip)

                    // Show a toast message
                    Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = vm.buttonContainerColor
                    )
                ) {
                    Text(text = "Copy")
                }
            }
        }
    }
}

fun generateQRCode(text: String): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bmp
}

@Composable
fun TeamDetails(vm: TeamViewModel,
                mainvm: mainViewModel,
                navController: NavController,
                mainPageNavController: NavController,
                mode: Int = 0
) {
   // val team = vm.selectedTeam.value
    val team by mainvm.selectedTeam.observeAsState()

    mainvm.setBab(true)
    mainvm.setTeamTab(0)

    mainvm.setLeftButton("Back", Icons.Default.ArrowBackIosNew, content = "BackToHome") {
        mainvm.setRightButton("", null, "") {}
        mainvm.setTitle("Teams")
        mainvm.setBab(false)
        mainPageNavController.popBackStack()
    }

    mainvm.setRightButton("Edit", Icons.Default.Edit, content = "EditTeam") {
        mainvm.setBab(false)
        mainvm.setBab(true)
        navController.navigate("editInfo")
        mainvm.setTitle("Edit " + vm.selectedTeam.value!!.name)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .background(Color(119, 189, 223))
                .fillMaxWidth()
                .height(200.dp)

        ) {
            Row(
                modifier = Modifier.
                fillMaxWidth())
            {

                // Left part of the box
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
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
                            team?.let { selectedTeam ->
                                if (selectedTeam.img.isNotEmpty()) {
                                AsyncImage(
                                    model = team!!.img,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }}

                // Right part of the box
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp, top = 80.dp, start = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = team!!.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 5.dp)
        ){
            Text(
                text = "Description:",
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 16.dp, end = 16.dp),
                color = Color(0, 69, 129)
            )
        }

        Row (
            modifier = Modifier
                .padding(top = 5.dp)
        ){
            Row (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .border(0.dp, Color.Black, RoundedCornerShape(5.dp))
                    .background(Color(246, 246, 246))
                    .height(100.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ){
                Text(text = team!!.description,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    fontSize = 18.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 5.dp)
        ){
            Text(
                text = "Category:",
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 16.dp, end = 16.dp),
                color = Color(0, 69, 129)
            )
        }

        Row (
            modifier = Modifier
                .padding(top = 5.dp)
        ){
            Row (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .border(0.dp, Color.Black, RoundedCornerShape(5.dp))
                    .background(Color(246, 246, 246))
                    .height(30.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = team!!.category,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    fontSize = 18.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 5.dp)
        ){
            Text(
                text = "Creation Date:",
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 16.dp, end = 16.dp),
                color = Color(0, 69, 129)
            )
        }

        Row (
            modifier = Modifier
                .padding(top = 5.dp)
        ){
            Row (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .border(0.dp, Color.Black, RoundedCornerShape(5.dp))
                    .background(Color(246, 246, 246))
                    .height(30.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = team!!.creationdate,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    fontSize = 18.sp
                )
            }
        }

        if (mode != 0) {
            Row (
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(onClick = { /*TODO*/ },
                    modifier = Modifier.width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(151,203,220)
                    )
                ) {
                    Text(text = "Cancel")
                }

                Button(onClick = { /*TODO*/ },
                    modifier = Modifier.width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0,69,129)
                    )
                ) {
                    Text(text = "Join")
                }
            }
        } else {
            Row (
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(onClick = {
                    mainvm.removeUserFromTeam(mainvm.loggedinUser.uid!!, mainvm.selectedTeamId.value!!)
                    mainPageNavController?.popBackStack()
                },
                    modifier = Modifier.width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(150,0,0)
                    )
                ){
                    Text(text = "Leave")
                }

            }
        }
    }
}