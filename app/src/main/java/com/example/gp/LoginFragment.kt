package com.example.gp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class LoginFragment : Fragment(R.layout.login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signupButton = view.findViewById<Button>(R.id.button)
        val openChatbotButton = view.findViewById<Button>(R.id.openChatbotButton)

        signupButton.setOnClickListener {
            // 회원가입 화면으로 이동
            val intent = Intent(requireContext(), SignupActivity::class.java)
            startActivity(intent)
        }

        openChatbotButton.setOnClickListener {
            // 챗봇 화면으로 이동(확인용)
            val intent = Intent(requireContext(), ChatbotActivity::class.java)
            startActivity(intent)
        }
    }
}
