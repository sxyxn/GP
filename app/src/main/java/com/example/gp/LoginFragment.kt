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
            val email = view.findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString().trim()
            val password = view.findViewById<EditText>(R.id.editTextTextPassword).text.toString().trim()

            // 입력값 검증
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 데이터베이스에서 사용자 가져오기
            val user = dbHelper.getUserById(email)

            if (user != null) {
                // 사용자 정보와 비밀번호 확인
                if (user.password == password) {
                    Toast.makeText(requireContext(), "로그인 성공! 환영합니다, ${user.name}.", Toast.LENGTH_SHORT).show()

                    // 메인 화면으로 이동
                    val intent = Intent(requireContext(), AfterLoginHomeActivity::class.java)
                    intent.putExtra("userEmail", user.id) // 사용자 이메일 전달
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 이메일이 존재하지 않는 경우
                Toast.makeText(requireContext(), "정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭 리스너 추가
        val signupButton = view.findViewById<Button>(R.id.button)
        signupButton.setOnClickListener {
            // SignupActivity로 이동
            val intent = Intent(requireContext(), SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
