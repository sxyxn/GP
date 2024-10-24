package com.example.gp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView // 추가된 import
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
<<<<<<< HEAD
        setContentView(R.layout.login)

        // UI 요소 초기화
        editTextEmail = findViewById(R.id.editTextTextEmailAddress)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        buttonLogin = findViewById(R.id.button2)
        buttonSignup = findViewById(R.id.button)

        // 시스템 바 인셋 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
=======
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
>>>>>>> ec17bf3860d1a7c930c13d80192bf381eabdbb21
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

<<<<<<< HEAD
        // 로그인 버튼 클릭 리스너
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            // 로그인 로직 추가 (예: Firebase, 서버 API 호출 등)
            Toast.makeText(this, "로그인 시도: $email", Toast.LENGTH_SHORT).show()
        }

        // 회원가입 버튼 클릭 리스너
        buttonSignup.setOnClickListener {
            // 회원가입 화면으로 전환
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
=======
        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                Log.d("MainActivity", "Navigating to ViewPagerFragment")
                findViewById<ImageView>(R.id.myImageView).visibility = View.GONE

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ViewPagerFragment())
                    .commit()
            }, 2000)
>>>>>>> ec17bf3860d1a7c930c13d80192bf381eabdbb21
        }
    }
}
