package com.example.gp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gp.models.User

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "UserDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_USER = "User"
        const val COLUMN_ID = "ID" // Email, Primary Key
        const val COLUMN_NAME = "Name"
        const val COLUMN_PASSWORD = "Password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USER (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    // 회원가입: User 추가 메서드
    fun insertUser(id: String, name: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_NAME, name)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USER, null, values)
    }

    // 로그인: ID로 User 검색 메서드
    fun getUserById(id: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_PASSWORD),
            "$COLUMN_ID = ?", arrayOf(id), null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            user = User(id, name, password)
        }
        cursor.close()
        return user
    }

    // 비밀번호 업데이트 메서드
    fun updatePassword(id: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PASSWORD, newPassword)
        }
        val rowsAffected = db.update(TABLE_USER, values, "$COLUMN_ID = ?", arrayOf(id))
        return rowsAffected > 0 // 1개 이상의 행이 업데이트되었는지 확인
    }

}
