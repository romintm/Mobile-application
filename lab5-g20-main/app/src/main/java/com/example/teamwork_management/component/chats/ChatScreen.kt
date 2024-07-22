package com.example.teamwork_management.component.chats


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamwork_management.dataClasses.Message
import com.example.teamwork_management.viewModels.ChatViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.teamwork_management.dataClasses.Chat
import com.example.teamwork_management.models.ChatListViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId:String,
               clvm: ChatListViewModel = viewModel(),
               vm:ChatViewModel = viewModel (),
               mainvm: mainViewModel,){



    mainvm.setTeamTab(0)
    val currentUser = mainvm.loggedinUser.uid
    val chat = clvm.getChatById(chatId)
    val messages by vm.messages.collectAsState()


    LaunchedEffect(currentUser){
        if (currentUser != null) {
            clvm.fetchChatsForuser(currentUser)
        }
        vm.loadMessages(chatId)
        if (currentUser != null) {
            vm.markMessageAsRead(chatId, currentUser)
        }
    }

    chat?.let {

        val otherUserId = it.participants.find { id -> id != currentUser }
        val otherUserImage = otherUserId?.let { id -> it.participantImages[id] }



    Scaffold (
        topBar  = {
            TopAppBar(title = {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 1.dp) ) {
                    otherUserImage?.let {imageUrl ->
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
                    chat?.chatName?.let { Text(it, color = Color.Black) }
                }
            },
                colors =  TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,

                ),
                modifier = Modifier.height(56.dp)
            )
        },
        content = {paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    //.padding(paddingValues)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    val sortedMessages = messages.sortedByDescending { it.timestamp }
                    LazyColumn(
                        reverseLayout = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    ) {
                        items(sortedMessages){ message ->
                            MessageListItem(message, isCurrentUser = message.senderId == currentUser)
                        }
                    }
                    MessageInput(onMessageSent = { message ->
                        if (currentUser != null) {
                            vm.sendMessage(chatId, message, currentUser, it.participants )
                        }
                    })
                }
            }

        }
    )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onMessageSent: (String)->Unit){
    var messageText by remember { mutableStateOf("") }

    Row(
       modifier = Modifier
           .fillMaxWidth()
           .padding(2.dp),
        //verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text(text = "Type a message") },
            shape = RoundedCornerShape(16.dp),
            maxLines = 3,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = Color.Black,
                containerColor = Color(0xFFF0F0F0),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Button(
            onClick = {
                if (messageText.isNotBlank()) {
                    onMessageSent(messageText)
                    messageText = ""
                }
            },
            modifier = Modifier.height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF004581),
                contentColor = Color.White
        )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Send",
                tint = Color.White // You can customize the icon color here
            )
        }


    }




}

@Composable
fun MessageListItem(message: Message, isCurrentUser: Boolean){

    var alignment = if (isCurrentUser) Alignment.Start else Alignment.End
    var background = if (isCurrentUser) Color(0xFF5DB075) else Color.White
    val textColor = if (isCurrentUser) Color.Black else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End  else Arrangement.Start
    ){
        Card(
            modifier = Modifier
                .widthIn(max = 300.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = background
            ),
            shape = RoundedCornerShape(8.dp) ,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                /*Text(
                    text = message.sender,
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))*/
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatTimestamp(message.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }


}

fun formatTimestamp(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp.seconds * 1000))
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewChatScreen() {
//    val navController = rememberNavController()
//    val mockViewModel = ChatViewModel()
//    val mockChat = Chat(
//        chatName = "General Chat",
//        lastMessage = "Hello, how are you?",
//        otherPersonImageUrl = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=2831&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
//    )
//
//    ChatScreen(chatId = "VzAbiLCxX4FeIfuF2KIw", vm = mockViewModel)
//}