package com.example.gp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.membership)

        // UI 요소 초기화
        editTextName = findViewById(R.id.editTextText)
        editTextEmail = findViewById(R.id.editTextTextEmailAddress2)
        editTextPassword = findViewById(R.id.editTextTextPassword2)
        editTextConfirmPassword = findViewById(R.id.editTextTextPassword3)
        buttonSignup = findViewById(R.id.button3)

        // 회원가입 버튼 클릭 리스너
        buttonSignup.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            // 간단한 입력 확인 로직
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 회원가입 로직 추가 (예: Firebase, 서버 API 호출 등)
                Toast.makeText(this, "회원가입 성공: $name", Toast.LENGTH_SHORT).show()
                // 회원가입 후 다른 액티비티로 이동하거나 필요한 추가 작업 수행
            }
        }
    }
}
