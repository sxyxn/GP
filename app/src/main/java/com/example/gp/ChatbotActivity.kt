package com.example.gp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatbotActivity : AppCompatActivity() {
    private lateinit var chatbotClient: ChatbotClient
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatbot_page)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val sendButton = findViewById<Button>(R.id.sendButton)

        chatbotClient = ChatbotClient()

        chatAdapter = ChatAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        chatAdapter.addMessage(ChatMessage("안녕하세요! 부산 관광지에 대해 물어보세요.", false))

        sendButton.setOnClickListener {
            val userInput = inputEditText.text.toString()
            if (userInput.isNotEmpty()) {
                chatAdapter.addMessage(ChatMessage(userInput, true))
                inputEditText.text.clear()

                chatbotClient.getChatResponse(userInput, object : Callback<ChatbotResponse> {
                    override fun onResponse(call: Call<ChatbotResponse>, response: Response<ChatbotResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                val reply = it.choices.firstOrNull()?.message?.content ?: "No response"
                                chatAdapter.addMessage(ChatMessage(reply, false))
                            }
                        } else {
                            chatAdapter.addMessage(ChatMessage("Error: ${response.errorBody()?.string()}", false))
                        }
                    }

                    override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
                        chatAdapter.addMessage(ChatMessage("Failed to connect: ${t.message}", false))
                    }
                })
            }
        }
    }
}
