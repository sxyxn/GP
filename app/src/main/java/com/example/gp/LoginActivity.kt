package com.example.gp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gp.data.UserDatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // 데이터베이스 초기화
        dbHelper = UserDatabaseHelper(this)

        // 로그인 버튼 클릭 리스너
        val loginButton = findViewById<Button>(R.id.button2)
        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString().trim()
            val password = findViewById<EditText>(R.id.editTextTextPassword).text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = dbHelper.getUserById(email)
            if (user != null && user.password == password) {
                Toast.makeText(this, "로그인 성공! 환영합니다, ${user.name}.", Toast.LENGTH_SHORT).show()

                // 메인 화면으로 이동
                val intent = Intent(this, AfterLoginHomeActivity::class.java)
                intent.putExtra("userEmail", user.id)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "이메일 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭 리스너
        val signupButton = findViewById<Button>(R.id.button)
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
