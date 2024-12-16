package com.example.gp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TravelPlanDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TravelPlans.db" // 데이터베이스 이름
        private const val DATABASE_VERSION = 1 // 데이터베이스 버전

        // 테이블 및 컬럼 정의
        const val TABLE_NAME = "TravelPlans"
        const val COLUMN_NO = "NO"
        const val COLUMN_USER_ID = "아이디"
        const val COLUMN_CATEGORY = "여행_카테고리"
        const val COLUMN_DATE = "날짜"
        const val COLUMN_TIME = "시간"
        const val COLUMN_DESTINATION = "여행지"
        const val COLUMN_ADDRESS = "여행지_주소"
        const val COLUMN_ACTIVITY = "활동"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 테이블 생성 쿼리
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_NO INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID TEXT NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_TIME TEXT NOT NULL,
                $COLUMN_DESTINATION TEXT NOT NULL,
                $COLUMN_ADDRESS TEXT NOT NULL,
                $COLUMN_ACTIVITY TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery) // 테이블 생성
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 기존 테이블 삭제 후 재생성
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 여행 계획 추가 메서드
    fun addTravelPlan(userId: String, category: String, date: String, time: String, destination: String, address: String, activity: String) {
        val db = writableDatabase // writableDatabase를 통해 DB에 쓰기 작업 수행
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_CATEGORY, category)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
            put(COLUMN_DESTINATION, destination)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_ACTIVITY, activity)
        }
        db.insert(TABLE_NAME, null, values) // 데이터를 테이블에 삽입
        db.close() // DB 연결 종료
    }
}
