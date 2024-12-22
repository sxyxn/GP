package com.example.gp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatResponseCallback : Callback<ChatbotResponse> {
    override fun onResponse(call: Call<ChatbotResponse>, response: Response<ChatbotResponse>) {
        if (response.isSuccessful) {
            val chatbotResponse = response.body()
            val reply = chatbotResponse?.choices?.firstOrNull()?.message?.content
            if (reply != null) {
                //API 응답 처리
                println("Chatbot response: $reply")
            }
        } else {
            //API 호출이 실패한 경우 에러 처리
            println("API Error: ${response.errorBody()?.string()}")
        }
    }

    override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
        //API 호출이 실패한 경우 에러 처리
        println("Request failed: ${t.message}")
    }
}
