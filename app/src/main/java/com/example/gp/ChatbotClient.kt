package com.example.gp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatbotClient {
    private val BASE_URL = "https://api.openai.com/v1/"
    private val service: ChatbotService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ChatbotService::class.java)
    }

    fun getChatResponse(userInput: String, callback: Callback<ChatbotResponse>) {
        val messages = listOf(
            Message(role = "user", content = userInput)
        )

        val request = ChatbotRequest(
            model = "gpt-3.5-turbo",
            messages = messages,
            max_tokens = 150,
            temperature = 0.7
        )

        val call = service.getCompletion(request)
        call.enqueue(callback)
    }
}
