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

        signupButton.setOnClickListener {
            // 회원가입 화면으로 이동
            val intent = Intent(requireContext(), SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
