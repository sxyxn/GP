package com.example.gp.data

data class TravelPlan(
    val no: Int,                // 여행 계획 번호
    val userId: String,         // 사용자 아이디
    val category: String,       // 여행 카테고리
    val date: String,           // 여행 날짜
    val time: String,           // 여행 시간
    val destination: String,    // 여행지
    val address: String,        // 여행지 주소
    val activity: String        // 활동
)
