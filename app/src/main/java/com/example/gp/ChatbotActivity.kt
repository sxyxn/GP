package com.example.gp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.data.TravelPlanDatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatbotActivity : AppCompatActivity() {
    private lateinit var chatbotClient: ChatbotClient
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var dbHelper: TravelPlanDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatbot_page)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val sendButton = findViewById<Button>(R.id.sendButton)

        chatbotClient = ChatbotClient()
        dbHelper = TravelPlanDatabaseHelper(this)

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

        // 버튼 클릭 리스너 설정
        chatAdapter.setOnButtonClickListener { place ->
            showPlanInputDialog(place)
        }
    }

    // 다이얼로그 생성 및 DB에 여행 계획 추가
    private fun showPlanInputDialog(place: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_plan_input, null)
        val dateEditText = dialogView.findViewById<EditText>(R.id.dateEditText)
        val timeEditText = dialogView.findViewById<EditText>(R.id.timeEditText)
        val activityEditText = dialogView.findViewById<EditText>(R.id.activityEditText)

        val dialog = AlertDialog.Builder(this)
            .setTitle("여행 계획 추가")
            .setView(dialogView)
            .setPositiveButton("추가") { _, _ ->
                val date = dateEditText.text.toString()
                val time = timeEditText.text.toString()
                val activity = activityEditText.text.toString()

                // DB에 여행 계획 추가
                val userId = "currentUser"  // 실제 사용자 아이디로 변경
                val category = ""
                val address = ""

                dbHelper.addTravelPlan(userId, category, date, time, place, address, activity)
                Toast.makeText(this, "$place 여행 계획이 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
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
}