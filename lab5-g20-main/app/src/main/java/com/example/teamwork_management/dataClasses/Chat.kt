package com.example.teamwork_management.dataClasses

import com.google.firebase.Timestamp


data class Chat(
    val chatId: String = "",
    val chatName: String = "",
    val lastMessage: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val unreadMessages: Map<String, Int> = mapOf(),
    val participantImages: Map<String, String> = mapOf(),
    val participants: List<String> = listOf()

)

data class Message(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
