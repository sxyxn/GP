package com.example.gp

import android.os.Bundle
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gp.data.TravelPlan
import com.example.gp.data.TravelPlanDatabaseHelper

class MyPlanActivity : AppCompatActivity() {

    private lateinit var dbHelper: TravelPlanDatabaseHelper
    private lateinit var travelPlansListView: ListView
    private lateinit var travelPlansTextView: TextView  // TextView 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myplan)

        // 데이터베이스 초기화
        dbHelper = TravelPlanDatabaseHelper(this)

        // CalendarView와 ListView 연결
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        travelPlansListView = findViewById(R.id.travelPlansListView)
        travelPlansTextView = findViewById(R.id.travelPlansTextView) // TextView 연결

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
                // 여행 계획이 없으면 ListView에 빈 어댑터 설정
                travelPlansListView.adapter = null
                travelPlansTextView.text = "해당 날짜에 여행 계획이 없습니다." // 메시지 표시
                travelPlansTextView.visibility = TextView.VISIBLE // 메시지 보이도록 설정
            } else {
                // 여행 계획을 ListView에 어댑터로 표시
                val adapter = TravelPlanListAdapter(this, travelPlans)
                travelPlansListView.adapter = adapter
                travelPlansTextView.visibility = TextView.GONE // 메시지 숨기기
            }
        } else {
            // 로그인되지 않은 상태일 경우 알림
            travelPlansTextView.text = "로그인 후 다시 시도해주세요."
            travelPlansTextView.visibility = TextView.VISIBLE
        }
    }
}
