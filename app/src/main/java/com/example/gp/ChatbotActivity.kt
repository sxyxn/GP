package com.example.gp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

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

                // 임의로 챗봇 응답 생성 (API 호출 없이 대체)
                val reply = """
                    1. 해운대 해수욕장
                    - 해운대 해수욕장
                    - 부산에서 가장 유명한 해수욕장으로 여름철에 많은 관광객이 방문합니다.
                    - 수상 스포츠나 해변 산책
                    2. 광안리 해수욕장
                    - 광안리 해수욕장
                    - 부산에서 야경이 아름다운 곳으로, 광안대교가 보이는 곳에서 해변 산책을 즐길 수 있습니다.
                    - 야경 감상, 바다 수영
                """.trimIndent()

                // 버튼 추가를 위한 추천 장소 파싱
                val recommendedPlaces = parseRecommendedPlaces(reply)
                for (place in recommendedPlaces) {
                    // 추천 장소에 대해 버튼 메시지 추가
                    chatAdapter.addMessage(ChatMessage(place, false, isButton = true))
                }
            }
        }
    }

    private fun parseRecommendedPlaces(reply: String): List<String> {
        val places = mutableListOf<String>()
        val regex = Regex("\\d+\\.\\s([^\n]+)")  // '숫자. 장소 이름' 형식으로 추출
        val matches = regex.findAll(reply)
        for (match in matches) {
            places.add(match.groupValues[1])  // 장소 이름만 추가
        }
        return places
    }

    private fun addToTravelPlan(place: String, category: String) {
        // 여행 계획에 장소 추가 - 데이터베이스 작업을 수행할 수 있음
        // 예시로 로그 출력
        println("$place 추가됨: $category")
    }
}
