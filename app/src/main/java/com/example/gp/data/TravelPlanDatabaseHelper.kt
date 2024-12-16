package com.example.gp.data

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
}
