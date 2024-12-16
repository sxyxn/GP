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

            // SharedPreferences에서 사용자 이메일 가져오기
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("user_id", null)

            if (userEmail != null) {
                // 선택한 날짜와 유저 이메일로 여행 계획 조회
                loadTravelPlansForUserAndDate(selectedDate, userEmail)
            } else {
                // 사용자 아이디가 없으면 로그인 화면으로 이동
                // 예: startActivity(Intent(this, LoginActivity::class.java))
                travelPlansTextView.text = "로그인 후 다시 시도해주세요."
            }
        }
    }

    // 유저 아이디와 날짜를 기준으로 여행 계획을 불러오는 함수
    private fun loadTravelPlansForUserAndDate(date: String, userId: String) {
        // 날짜와 사용자 ID를 기준으로 여행 계획을 가져옵니다.
        val travelPlans = dbHelper.getPlansByUserIdAndDate(userId, date)

        if (travelPlans.isNotEmpty()) {
            // 여행 계획을 TextView에 표시
            travelPlansTextView.text = buildTravelPlansText(travelPlans)
        } else {
            // 여행 계획이 없으면 알림
            travelPlansTextView.text = "이 날짜에 대한 여행 계획이 없습니다."
        }
    }

    // 여행 계획을 표시할 텍스트를 빌드하는 함수
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
