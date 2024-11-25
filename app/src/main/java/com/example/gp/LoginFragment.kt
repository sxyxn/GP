package com.example.gp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gp.data.UserDatabaseHelper

class LoginFragment : Fragment(R.layout.login) {

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터베이스 초기화
        dbHelper = UserDatabaseHelper(requireContext())

        // 로그인 버튼 클릭 리스너
        val loginButton = view.findViewById<Button>(R.id.button2)
        loginButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
            val password = view.findViewById<EditText>(R.id.editTextTextPassword).text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = dbHelper.getUserById(email)
            if (user != null && user.password == password) {
                Toast.makeText(requireContext(), "로그인 성공! 환영합니다, ${user.name}.", Toast.LENGTH_SHORT).show()

                // 메인 화면으로 이동
                val intent = Intent(requireContext(), AfterLoginHomeActivity::class.java)
                startActivity(intent)
                // 현재 Activity 종료
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "이메일 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
