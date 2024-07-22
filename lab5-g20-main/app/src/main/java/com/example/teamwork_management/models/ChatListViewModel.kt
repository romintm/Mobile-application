package com.example.teamwork_management.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamwork_management.dataClasses.Chat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatListViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    val userFB = FirebaseAuth.getInstance().currentUser
    val userFBId = userFB?.uid

    private val _newChatId = MutableStateFlow<String?>(null)
    val newChatId: StateFlow<String?> = _newChatId


    fun fetchChatsForuser(userId: String) {
        viewModelScope.launch {
            firestore.collection("chats")
                .whereArrayContains("participants", userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    val chatList = mutableListOf<Chat>()
                    if (snapshot != null) {
                        for (document in snapshot) {
                            var chat =
                                document.toObject(Chat::class.java).copy(chatId = document.id)
                            chatList.add(chat)
                        }
                    }
                    _chats.value = chatList
                }
        }
    }

    fun getChatById(chatId: String): Chat? {
        return _chats.value.find { it.chatId == chatId }
    }

    fun createNewChat(
        userId: String,
        otherUserId: String,
        chatName: String,
        userURLImage: String,
        otherUrlImage: String
    ) {

        viewModelScope.launch {

            val existingChatQuery = firestore.collection("chats")
                .whereArrayContains("participants", userId)
                .get()
                .await()

            val existingChat = existingChatQuery.documents.find { document ->
                val chat = document.toObject(Chat::class.java)
                chat?.participants?.contains(otherUserId) == true
            }

            if (existingChat != null) {
                _newChatId.value = existingChat.id
            } else {

                val newChatId = firestore.collection("chats").document().id
                val newChat = Chat(
                    chatName = chatName,
                    lastMessage = "",
                    timestamp = com.google.firebase.Timestamp.now(),
                    unreadMessages = mapOf(userId to 0, otherUserId to 0),
                    participantImages = mapOf(
                        userId to userURLImage,
                        otherUserId to otherUrlImage
                    ),
                    participants = listOf(userId, otherUserId)
                )

                firestore.collection("chats")
                    .document(newChatId)
                    .set(newChat)
                    .await()
                _newChatId.value = newChatId
            }
        }
    }

    fun resetNewChatId() {
        _newChatId.value = null
    }
//close the class
}




    /*private fun getMockChats(): List<Chat> {
        return listOf(
            Chat(
                chatId = "1",
                chatName = "General Chat",
                lastMessage = "Hello, how are you?",
                timestamp = Timestamp.now() - 100000,
                unreadMessage = 2,
                otherPersonImageUrl = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=2831&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            ),
            Chat(
                chatId = "2",
                chatName = "Work Chat",
                lastMessage = "Please review the document.",
                timestamp = System.currentTimeMillis() - 200000,
                unreadMessage = 1,
                otherPersonImageUrl = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=2831&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            ),
            Chat(
                chatId = "3",
                chatName = "Friends Chat",
                lastMessage = "Are we meeting tonight?",
                timestamp = System.currentTimeMillis() - 300000,
                unreadMessage = 3,
                otherPersonImageUrl = "https://static.vecteezy.com/system/resources/previews/025/220/125/non_2x/picture-a-captivating-scene-of-a-tranquil-lake-at-sunset-ai-generative-photo.jpg"
            ),
            Chat(
                chatId = "4",
                chatName = "General Chat",
                lastMessage = "Hello, how are you?",
                timestamp = System.currentTimeMillis() - 100000,
                unreadMessage = 4,
                otherPersonImageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            ),
            Chat(
                chatId = "5",
                chatName = "Work Chat",
                lastMessage = "Please review the document.",
                timestamp = System.currentTimeMillis() - 200000,
                unreadMessage = 1,
                otherPersonImageUrl = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=2831&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            ),
            Chat(
                chatId = "6",
                chatName = "Friends Chat",
                lastMessage = "Are we meeting tonight?",
                timestamp = System.currentTimeMillis() - 300000,
                unreadMessage = 1,
                otherPersonImageUrl = "https://static.vecteezy.com/system/resources/previews/025/220/125/non_2x/picture-a-captivating-scene-of-a-tranquil-lake-at-sunset-ai-generative-photo.jpg"
            ),
            Chat(
                chatId = "7",
                chatName = "General Chat",
                lastMessage = "Hello, how are you?",
                timestamp = System.currentTimeMillis() - 100000,
                unreadMessage = 3,
                otherPersonImageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            ),
            Chat(
                chatId = "8",
                chatName = "Work Chat",
                lastMessage = "Please review the document.",
                timestamp = System.currentTimeMillis() - 200000,
                unreadMessage = 2,
                otherPersonImageUrl = "https://images.unsplash.com/photo-1511367461989-f85a21fda167?q=80&w=2831&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            ),
            Chat(
                chatId = "9",
                chatName = "Friends Chat",
                lastMessage = "Are we meeting tonight?",
                timestamp = System.currentTimeMillis() - 300000,
                unreadMessage = 1,
                otherPersonImageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            )
        )
    }*/

