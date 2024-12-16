package com.example.gp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gp.data.TravelPlanDatabaseHelper


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = TravelPlanDatabaseHelper(this)
        dbHelper.writableDatabase // 데이터베이스를 참조하면 테이블이 자동 생성됩니다.

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                Log.d("MainActivity", "Navigating to ViewPagerFragment")
                findViewById<ImageView>(R.id.myImageView).visibility = View.GONE

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ViewPagerFragment())
                    .commit()
            }, 2000)
        }
    }
}