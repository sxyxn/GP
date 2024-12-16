package com.example.gp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.data.TravelPlanDatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

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
        val dateTextView = dialogView.findViewById<TextView>(R.id.dateTextView)
        val timeTextView = dialogView.findViewById<TextView>(R.id.timeTextView)
        val activityEditText = dialogView.findViewById<EditText>(R.id.activityEditText)

        val dialog = AlertDialog.Builder(this)
            .setTitle("여행 계획 추가")
            .setView(dialogView)
            .setPositiveButton("추가") { _, _ ->
                val date = dateTextView.text.toString()
                val time = timeTextView.text.toString()
                val activity = activityEditText.text.toString()

                // SharedPreferences에서 로그인한 사용자 ID를 가져옴
                val userId = getUserIdFromPreferences(this)

                val category = ""
                val address = ""

                // DB에 여행 계획 추가
                dbHelper.addTravelPlan(userId, category, date, time, place, address, activity)
                Toast.makeText(this, "$place 여행 계획이 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()

        // 날짜 선택
        dateTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$year-${month + 1}-$dayOfMonth"
                    dateTextView.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // 시간 선택
        timeTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    timeTextView.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }
    }

    // SharedPreferences에서 로그인한 사용자 ID를 가져오는 함수
    private fun getUserIdFromPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", "") ?: ""
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
