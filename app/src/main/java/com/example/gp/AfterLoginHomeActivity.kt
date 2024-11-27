package com.example.gp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AfterLoginHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.after_login_home)

        // gotogp 버튼 클릭 리스너 추가
        val gotoGpButton = findViewById<ImageButton>(R.id.imageButton)
        gotoGpButton.setOnClickListener {
            // ChatbotActivity로 이동
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
        }
    }
}
