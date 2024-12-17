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
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatbotActivity : AppCompatActivity() {
    private lateinit var chatbotClient: ChatbotClient
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var dbHelper: TravelPlanDatabaseHelper
    private val placeAddressMap = mutableMapOf<String, String>() // 장소 이름과 주소 매핑

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
                // 사용자 입력을 화면에 추가
                chatAdapter.addMessage(ChatMessage(userInput, true))
                inputEditText.text.clear()

                // 챗봇 API 호출
                chatbotClient.getChatResponse(userInput, object : Callback<ChatbotResponse> {
                    override fun onResponse(call: Call<ChatbotResponse>, response: Response<ChatbotResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                // 챗봇의 응답 메시지를 가져옴
                                val reply = it.choices.firstOrNull()?.message?.content ?: "No response"
                                chatAdapter.addMessage(ChatMessage(reply, false))

                                // 관광지 추천에 대한 응답을 파싱
                                val recommendedPlaces = parseRecommendedPlaces(reply)
                                for ((name, address) in recommendedPlaces) {
                                    // 이름과 주소를 매핑하고 버튼 메시지 추가
                                    placeAddressMap[name] = address
                                    chatAdapter.addMessage(ChatMessage(name, false, isButton = true))
                                }
                            }
                        } else {
                            // 오류 응답 처리
                            chatAdapter.addMessage(ChatMessage("Error: ${response.errorBody()?.string()}", false))
                        }
                    }

                    override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
                        // 네트워크 오류 처리
                        chatAdapter.addMessage(ChatMessage("Failed to connect: ${t.message}", false))
                    }
                })
            }
        }


        // 버튼 클릭 리스너 설정
        chatAdapter.setOnButtonClickListener { placeName ->
            val address = placeAddressMap[placeName] ?: ""
            showPlanInputDialog(placeName, address)
        }
    }

    // 다이얼로그 생성 및 DB에 여행 계획 추가
    private fun showPlanInputDialog(place: String, address: String) {
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
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$year-${month + 1}-$dayOfMonth"
                    dateTextView.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 시간 선택
        timeTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    timeTextView.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    // SharedPreferences에서 로그인한 사용자 ID를 가져오는 함수
    private fun getUserIdFromPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", "") ?: ""
    }

    // 추천 장소 이름과 주소를 파싱하는 함수
    private fun parseRecommendedPlaces(reply: String): List<Pair<String, String>> {
        val places = mutableListOf<Pair<String, String>>()
        val regex = Regex("\\d+\\.\\s'([^']+)'\\(([^)]+)\\)") // 이름과 주소 추출
        val matches = regex.findAll(reply)
        for (match in matches) {
            val name = match.groupValues[1] // 관광지 이름
            val address = match.groupValues[2] // 관광지 주소
            places.add(Pair(name, address))
        }
        return places
    }
}
