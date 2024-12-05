package com.example.gp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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

                                // 관광지 추천에 대한 응답 처리
                                val recommendedPlaces = parseRecommendedPlaces(reply)
                                for (place in recommendedPlaces) {
                                    // 답변에 해당하는 추천 장소 출력
                                    chatAdapter.addMessage(ChatMessage("추천된 장소: $place", false))

                                    // "추가하기" 버튼을 대신 텍스트로 표시하여 위치를 확인
                                    chatAdapter.addMessage(ChatMessage("버튼: $place 추가하기 (버튼 위치)", false))

                                    // 실제 버튼을 UI에 추가하려면 아래 코드를 사용해야 합니다.
                                    // val addButton = Button(this@ChatbotActivity).apply {
                                    //     text = "$place 추가하기"
                                    //     setOnClickListener {
                                    //         showAddToPlanDialog(place)
                                    //     }
                                    // }
                                    // 여기서 RecyclerView 아이템으로 버튼을 추가하거나 적절한 뷰에 버튼을 추가할 수 있습니다.
                                }
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

    private fun parseRecommendedPlaces(reply: String): List<String> {
        val places = mutableListOf<String>()
        val regex = Regex("\\d+\\.\\s([^\n]+)")
        val matches = regex.findAll(reply)
        for (match in matches) {
            places.add(match.groupValues[1])
        }
        return places
    }

    private fun addToTravelPlan(place: String, category: String) {
        // 여행 계획에 장소 추가 - 데이터베이스 작업을 수행할 수 있음
        // 예시로 로그 출력
        println("$place 추가됨: $category")
    }
}
