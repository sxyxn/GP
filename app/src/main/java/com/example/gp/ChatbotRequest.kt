package com.example.gp

data class ChatbotRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>, // 메시지 리스트
    val max_tokens: Int = 500,
    val temperature: Double = 0.7
)

data class Message(
    val role: String, // "user", "assistant", "system"
    val content: String // 메시지 내용
)
