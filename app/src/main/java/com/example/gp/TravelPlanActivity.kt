package com.example.gp

import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gp.data.TravelPlanDatabaseHelper
import com.example.gp.data.TravelPlan

class TravelPlanActivity : AppCompatActivity() {

    private lateinit var dbHelper: TravelPlanDatabaseHelper
    private lateinit var travelPlansTextView: TextView // 여행 계획을 표시할 TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myplan)

        dbHelper = TravelPlanDatabaseHelper(this)
        travelPlansTextView = findViewById(R.id.travelPlansTextView)

        // CalendarView에 새로운 날짜 선택 리스너 설정
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // 날짜 포맷을 "YYYY-MM-DD"로 지정
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            val userEmail = "example@example.com" // 로그인한 유저의 아이디를 가져와야 합니다.

            // 선택한 날짜와 유저 이메일로 여행 계획 조회
            loadTravelPlansForDate(selectedDate, userEmail)
        }
    }

    private fun loadTravelPlansForDate(date: String, userId: String) {
        // 날짜와 사용자 ID를 기준으로 여행 계획을 가져옵니다.
        val travelPlans = dbHelper.getPlansByUserIdAndDate(date, userId)

        if (travelPlans.isNotEmpty()) {
            // 여행 계획을 TextView에 표시
            travelPlansTextView.text = buildTravelPlansText(travelPlans)
        } else {
            // 여행 계획이 없으면 알림
            travelPlansTextView.text = "이 날짜에 대한 여행 계획이 없습니다."
        }
    }

    private fun buildTravelPlansText(plans: List<TravelPlan>): String {
        val sb = StringBuilder()
        for (plan in plans) {
            sb.append("여행지: ${plan.destination}\n")
            sb.append("시간: ${plan.time}\n")
            sb.append("활동: ${plan.activity}\n\n")
        }
        return sb.toString()
    }
}
