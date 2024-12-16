package com.example.gp.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues


class TravelPlanDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TravelPlans.db"
        private const val DATABASE_VERSION = 1


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
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getPlansByUserIdAndDate(userId: String, date: String): List<TravelPlan> {
        val db = readableDatabase
        val plans = mutableListOf<TravelPlan>()

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USER_ID = ? AND $COLUMN_DATE = ? ORDER BY $COLUMN_TIME"
        val cursor: Cursor = db.rawQuery(query, arrayOf(userId, date))

        if (cursor.moveToFirst()) {
            do {
                val noIndex = cursor.getColumnIndex(COLUMN_NO)
                val userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID)
                val categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY)
                val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
                val timeIndex = cursor.getColumnIndex(COLUMN_TIME)
                val destinationIndex = cursor.getColumnIndex(COLUMN_DESTINATION)
                val addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS)
                val activityIndex = cursor.getColumnIndex(COLUMN_ACTIVITY)

                if (noIndex != -1 && userIdIndex != -1 && categoryIndex != -1 &&
                    dateIndex != -1 && timeIndex != -1 && destinationIndex != -1 &&
                    addressIndex != -1 && activityIndex != -1) {

                    val plan = TravelPlan(
                        cursor.getInt(noIndex),
                        cursor.getString(userIdIndex),
                        cursor.getString(categoryIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(timeIndex),
                        cursor.getString(destinationIndex),
                        cursor.getString(addressIndex),
                        cursor.getString(activityIndex)
                    )
                    plans.add(plan)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return plans
    }

    // 여행 계획 추가 메서드
    fun addTravelPlan(userId: String, category: String, date: String, time: String, destination: String, address: String, activity: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_CATEGORY, category)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
            put(COLUMN_DESTINATION, destination)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_ACTIVITY, activity)
        }
        db.insert(TABLE_NAME, null, values)
        db.close() // DB 연결 종료
    }

}
