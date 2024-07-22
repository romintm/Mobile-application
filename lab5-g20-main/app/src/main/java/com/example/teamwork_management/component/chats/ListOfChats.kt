package com.example.teamwork_management.component.chats

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.teamwork_management.dataClasses.Chat
import com.example.teamwork_management.models.ChatListViewModel
import com.example.teamwork_management.viewModels.UserViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ChatListScreen(navController: NavController, vm: ChatListViewModel, mainPageNavController: NavController, mainvm: mainViewModel, uvm: UserViewModel){

    mainvm.setTeamTab(0)
    val user by uvm.user.collectAsState()
    val currentUser = user!!.uid
    val chatList by vm.chats.collectAsState()

    var query by remember { mutableStateOf("") }

    LaunchedEffect(currentUser){
        vm.fetchChatsForuser(currentUser)
    }

    //Data user from google

//    println("----------- User name to check ${user?.displayName} -----------")
//    println("----------- User email to check ${user?.email} ----------------")
//    println("----------- User uid check ${user?.uid} ----------------")
//    println("----------- User photo check ${user?.photoUrl} ----------------")

    val filteredChats = chatList.filter {
        it.chatName.contains(query, ignoreCase = true) ||
                it.lastMessage.contains(query)
    }

    Column() {
        Text(text = "Chats", textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = Color(0XFF004581),
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.ExtraBold) )
        SearchBar(query = query, onQueryChanged = { query = it })
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 1.dp, horizontal = 1.dp)){
                items(filteredChats) {
                        chat ->
                    ChatListItem(chat= chat, currentUser= currentUser, onClick = {
                    navController.navigate("chat/${chat.chatId}")
                    mainvm.setLeftButton(
                        "Back",
                        Icons.Default.ArrowBackIosNew,
                        content = "BackToHome"
                    ) {
                        navController.popBackStack()
                        mainvm.setLeftButton(
                            "Back",
                            Icons.Default.ArrowBackIosNew,
                            content = "BackToHome"
                        ) {
                            mainPageNavController.popBackStack()
                            mainvm.setLeftButton("Home", Icons.Default.Home, content = "Home") {}
                            mainvm.setTitle("Teams")
                        }
                    }
                })
                }
            }

    }

}

@Composable
fun ChatListItem(chat: Chat, currentUser: String, onClick:() -> Unit){

    val unreadCount = chat.unreadMessages[currentUser] ?: 0
    val otherUserId = chat.participants.find { id -> id != currentUser }
    val otherUserImage: String

    if (otherUserId== null){
        otherUserImage = chat.participantImages[currentUser].toString()
    }else{
        otherUserImage = otherUserId?.let { id -> chat.participantImages[id] }.toString()
    }



    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 1.dp)
        .clickable (onClick = onClick ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color(0XFFBD9FE9)),
        //shape = RoundedCornerShape(8.dp)
        ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            otherUserImage?.let { imageUrl ->
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.chatName,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)

                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.lastMessage,
                    style = TextStyle(fontSize = 7.sp, fontWeight = FontWeight.Light)
                )
            }
            if (unreadCount > 0){
                Box(modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xff004581), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center){
                    Text(
                        text = unreadCount.toString(),
                        color = Color.White,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    )
                }
            }
        }
    }
}

fun formatTimeStamp(timeStamp: Long): String{
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timeStamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query:String, onQueryChanged: (String) -> Unit){

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        contentAlignment = Alignment.Center){
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(8.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            placeholder = { Text("Search") },
            trailingIcon  = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }



}

