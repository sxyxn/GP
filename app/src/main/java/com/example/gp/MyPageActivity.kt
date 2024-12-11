    package com.example.gp

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.example.gp.data.UserDatabaseHelper

    class MyPageActivity : AppCompatActivity() {

        private lateinit var dbHelper: UserDatabaseHelper

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.mypage)

            // 데이터베이스 초기화
            dbHelper = UserDatabaseHelper(this)

            // Intent에서 이메일 가져오기
            val userEmail = intent.getStringExtra("userEmail")

            if (userEmail != null) {
                // 데이터베이스에서 사용자 정보 가져오기
                val user = dbHelper.getUserById(userEmail)

                val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
                val nameEditText = findViewById<EditText>(R.id.editTextText)
                val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
                val confirmPasswordEditText = findViewById<EditText>(R.id.editTextTextPassword2)

                if (user != null) {
                    emailEditText.setText(user.id) // 이메일 설정
                    nameEditText.setText(user.name) // 이름 설정

                    // 이름과 이메일 수정 불가능하게 설정
                    emailEditText.isFocusable = false
                    emailEditText.isClickable = false
                    emailEditText.isEnabled = false

                    nameEditText.isFocusable = false
                    nameEditText.isClickable = false
                    nameEditText.isEnabled = false
                }

                // "수정하기" 버튼 클릭 리스너 추가
                val updateButton = findViewById<Button>(R.id.button)
                updateButton.setOnClickListener {
                    val newPassword = passwordEditText.text.toString().trim()
                    val confirmPassword = confirmPasswordEditText.text.toString().trim()

                    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else if (newPassword != confirmPassword) {
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 비밀번호 업데이트
                        val result = dbHelper.updatePassword(userEmail, newPassword)
                        if (result) {
                            Toast.makeText(this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                            passwordEditText.text.clear()
                            confirmPasswordEditText.text.clear()
                        } else {
                            Toast.makeText(this, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                println("이메일 전달 실패")
            }

            // "로그아웃" 버튼 클릭 리스너 추가
            val logoutButton = findViewById<Button>(R.id.logoutButton)
            logoutButton.setOnClickListener {
                // 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 스택 초기화
                startActivity(intent)
                finish() // 현재 Activity 종료
            }
        }
    }
