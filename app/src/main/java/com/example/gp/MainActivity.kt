package com.example.gp

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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
