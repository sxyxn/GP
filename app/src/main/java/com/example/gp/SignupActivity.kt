package com.example.gp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gp.data.UserDatabaseHelper

class SignupActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.membership)

        // 데이터베이스 초기화
        dbHelper = UserDatabaseHelper(this)

        // 회원가입 버튼 클릭 리스너
        val buttonSignup = findViewById<Button>(R.id.button3)
        buttonSignup.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
            val name = findViewById<EditText>(R.id.editTextText).text.toString()
            val password = findViewById<EditText>(R.id.editTextTextPassword2).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.editTextTextPassword3).text.toString()

            // 입력 확인
            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이메일 형식 확인
            if (!isValidEmail(email)) {
                Toast.makeText(this, "유효한 이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 비밀번호 확인
            if (password != confirmPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 데이터베이스에 사용자 추가
            val result = dbHelper.insertUser(email, name, password)
            if (result != -1L) {
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                finish() // 로그인 화면으로 돌아가기
            } else {
                Toast.makeText(this, "회원가입 실패: 이미 존재하는 사용자입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 이메일 유효성 검사 함수
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }
}
