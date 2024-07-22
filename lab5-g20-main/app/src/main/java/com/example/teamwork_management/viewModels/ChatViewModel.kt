package com.example.teamwork_management.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamwork_management.dataClasses.Chat
import com.example.teamwork_management.dataClasses.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun loadMessages(chatId:String){
        viewModelScope.launch {
            firestore.collection("messages")
                .whereEqualTo("chatId", chatId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null){
                        return@addSnapshotListener
                    }

                    val messageList = mutableListOf<Message>()
                    if (snapshot != null) {
                        for (document in snapshot){
                            val message = document.toObject(Message::class.java)
                            messageList.add(message)
                        }
                    }
                    _messages.value = messageList
                }
        }
    }

    fun sendMessage(chatId: String, content: String, senderId: String, participants: List<String>) {
        val messageId = firestore.collection("messages").document().id
        val newMessage = Message(
            messageId = messageId,
            chatId = chatId,
            senderId = senderId,  // Replace with the actual sender
            content = content,
            timestamp = Timestamp.now()
        )
        firestore.collection("messages").document(messageId).set(newMessage)

        val chatRef = firestore.collection("chats").document(chatId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(chatRef)
            val chat = snapshot.toObject(Chat::class.java)
            val unreadMessages = chat?.unreadMessages?.toMutableMap() ?: mutableMapOf()
            for (participant in participants) {
                if (participant != senderId) {
                    unreadMessages[participant] = (unreadMessages[participant] ?: 0) + 1
                }
            }
            transaction.update(chatRef, "unreadMessages", unreadMessages)
        }
    }

    fun markMessageAsRead(chatId: String, userId:String){
        val chatRef = firestore.collection("chats").document(chatId)
        firestore.runTransaction{transaction ->
            val snapshot = transaction.get(chatRef)
            val chat = snapshot.toObject(Chat::class.java)
            val unreadMessages = chat?.unreadMessages?.toMutableMap() ?: mutableMapOf()
            unreadMessages[userId] = 0
            transaction.update(chatRef, "unreadMessages", unreadMessages)
        }
    }

    /*private fun getMockMessages(chatId: String): List<Message>{
        return listOf(
            Message(
                messageId = "1",
                chatId = chatId,
                sender = "User1",
                content = "Hello!",
                timestamp = System.currentTimeMillis() - 1000
            ),
            Message(
                messageId = "2",
                chatId = chatId,
                sender = "User2",
                content = "Hi, how are you?",
                timestamp = System.currentTimeMillis() - 2000
            ),
            Message(
                messageId = "3",
                chatId = chatId,
                sender = "User1",
                content = "I'm good, thanks!",
                timestamp = System.currentTimeMillis() - 3000
            ),
            Message(
                messageId = "2",
                chatId = chatId,
                sender = "User2",
                content = "Hi, how are you?",
                timestamp = System.currentTimeMillis() - 2000
            ),
        )
    }*/



}