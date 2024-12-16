package com.example.gp

import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gp.data.TravelPlanDatabaseHelper

class MyPlanActivity : AppCompatActivity() {

    private lateinit var dbHelper: TravelPlanDatabaseHelper
    private lateinit var travelPlansTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myplan)

        // 데이터베이스 초기화
        dbHelper = TravelPlanDatabaseHelper(this)

        // CalendarView와 TextView 연결
        val calendarView: CalendarView = findViewById(R.id.calendarView) // 달력 위젯
        travelPlansTextView = findViewById(R.id.travelPlansTextView) // 여행 계획을 표시할 TextView

        // 날짜 선택 리스너 설정
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth" // yyyy-MM-dd 형식으로 날짜 설정
            showTravelPlansForDate(selectedDate) // 해당 날짜의 여행 계획을 보여주는 메서드 호출
        }
    }

    private fun showTravelPlansForDate(date: String) {
        // SharedPreferences에서 로그인된 유저 아이디 가져오기
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_id", null)

        if (userEmail != null) {
            // 해당 날짜와 유저 아이디에 맞는 여행 계획을 데이터베이스에서 조회
            val travelPlans = dbHelper.getPlansByUserIdAndDate(userEmail, date)

            // 조회된 여행 계획이 없으면 "여행 계획이 없습니다"를 표시
            if (travelPlans.isEmpty()) {
                travelPlansTextView.text = "해당 날짜에 여행 계획이 없습니다."
            } else {
                // 여행 계획을 텍스트로 표시
                val plansText = StringBuilder()
                for (plan in travelPlans) {
                    plansText.append("시간: ${plan.time}, 여행지: ${plan.destination}, 활동: ${plan.activity}\n")
                }
                travelPlansTextView.text = plansText.toString()
            }
        } else {
            // 로그인되지 않은 상태일 경우 알림
            travelPlansTextView.text = "로그인 후 다시 시도해주세요."
        }
    }
}
