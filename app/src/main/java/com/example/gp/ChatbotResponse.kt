package com.example.gp

import com.google.gson.annotations.SerializedName

data class ChatbotResponse(
    @SerializedName("choices")
    val choices: List<Choice>
)

data class Choice(
    @SerializedName("message")
    val message: Message
)
