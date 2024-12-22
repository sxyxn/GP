package com.example.gp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatbotService {
    @Headers(
        "Content-Type: application/json",

        //"Authorization: Bearer sk-proj-"

        "Authorization: Bearer API 키 입력"
    )
    @POST("chat/completions")
    fun getCompletion(@Body request: ChatbotRequest): Call<ChatbotResponse>
}