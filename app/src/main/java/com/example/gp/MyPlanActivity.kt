package com.example.gp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gp.data.TravelPlan
import com.example.gp.data.TravelPlanDatabaseHelper
import java.util.Calendar

class MyPlanActivity : AppCompatActivity() {

    private lateinit var dbHelper: TravelPlanDatabaseHelper
    private lateinit var travelPlansListView: ListView
    private lateinit var travelPlansTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myplan)

        dbHelper = TravelPlanDatabaseHelper(this)
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        travelPlansListView = findViewById(R.id.travelPlansListView)
        travelPlansTextView = findViewById(R.id.travelPlansTextView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            showTravelPlansForDate(selectedDate)
        }

        travelPlansListView.setOnItemClickListener { _, _, position, _ ->
            val selectedDate = getSelectedDateFromCalendar(calendarView)
            val travelPlan = travelPlansListView.adapter.getItem(position) as TravelPlan
            showEditPlanDialog(travelPlan, selectedDate)
        }
    }

    private fun showEditPlanDialog(travelPlan: TravelPlan, date: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("여행 계획 수정")
        dialogBuilder.setMessage("날짜: $date")

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_plan, null)
        val timeInput = dialogView.findViewById<EditText>(R.id.timeInput)
        val placeInput = dialogView.findViewById<EditText>(R.id.placeInput)
        val activityInput = dialogView.findViewById<EditText>(R.id.activityInput)

        timeInput.setText(travelPlan.time)
        placeInput.setText(travelPlan.destination)
        activityInput.setText(travelPlan.activity)

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("저장") { _, _ ->
            val newTime = timeInput.text.toString()
            val newPlace = placeInput.text.toString()
            val newActivity = activityInput.text.toString()

            if (newTime.isNotBlank() && newPlace.isNotBlank() && newActivity.isNotBlank()) {
                // 새롭게 수정된 TravelPlan 객체 생성
                val updatedTravelPlan = travelPlan.copy(
                    time = newTime,
                    destination = newPlace,
                    activity = newActivity
                )

                // 데이터베이스 업데이트
                val rowsAffected = dbHelper.updatePlan(updatedTravelPlan)
                if (rowsAffected > 0) {
                    Toast.makeText(this, "계획이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    showTravelPlansForDate(date) // UI 갱신
                } else {
                    Toast.makeText(this, "데이터베이스 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 삭제 버튼 추가
        dialogBuilder.setNegativeButton("삭제") { _, _ ->
            AlertDialog.Builder(this)
                .setTitle("여행 계획 삭제")
                .setMessage("정말로 이 계획을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    val rowsAffected = dbHelper.deletePlan(travelPlan.no)
                    if (rowsAffected > 0) {
                        Toast.makeText(this, "계획이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        showTravelPlansForDate(date) // 삭제 후 UI 갱신
                    } else {
                        Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("취소", null)
                .show()
        }

        dialogBuilder.setNeutralButton("취소", null) // 취소 버튼
        dialogBuilder.show()
    }


    private fun showTravelPlansForDate(date: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_id", null)

        if (userEmail != null) {
            val travelPlans = dbHelper.getPlansByUserIdAndDate(userEmail, date)
            if (travelPlans.isEmpty()) {
                travelPlansListView.adapter = null
                travelPlansTextView.text = "해당 날짜에 여행 계획이 없습니다."
                travelPlansTextView.visibility = TextView.VISIBLE
            } else {
                val adapter = TravelPlanListAdapter(this, travelPlans)
                travelPlansListView.adapter = adapter
                travelPlansTextView.visibility = TextView.GONE
            }
        } else {
            travelPlansTextView.text = "로그인 후 다시 시도해주세요."
            travelPlansTextView.visibility = TextView.VISIBLE
        }
    }


    private fun getSelectedDateFromCalendar(calendarView: CalendarView): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = calendarView.date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1  // 월은 0부터 시작하므로 1을 더해야 합니다.
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }

}
