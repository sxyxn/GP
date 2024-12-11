package com.example.gp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AfterLoginHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.after_login_home)

        val userEmail = intent.getStringExtra("userEmail")


        // gotogp 버튼 클릭 리스너 추가
        val gotoGpButton = findViewById<ImageButton>(R.id.imageButton)
        gotoGpButton.setOnClickListener {
            // ChatbotActivity로 이동
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
        }
        // gotolist 버튼 클릭 리스너 추가
        val gotolist = findViewById<ImageButton>(R.id.imageButton2)
        gotolist.setOnClickListener {
            // MyPlanActivity로 이동
            val intent = Intent(this, MyPlanActivity::class.java)
            startActivity(intent)
        }

        // My Page 버튼 클릭 리스너 추가
        val myPageButton = findViewById<ImageButton>(R.id.imageButton3)
        myPageButton.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            intent.putExtra("userEmail", userEmail) // 이메일 전달
            startActivity(intent)
        }
    }


}
