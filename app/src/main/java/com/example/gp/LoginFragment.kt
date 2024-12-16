package com.example.gp

import android.content.Intent
import android.content.SharedPreferences
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

                    // 사용자 ID를 SharedPreferences에 저장
                    val sharedPreferences = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("user_id", user.id)  // 사용자 ID 저장
                    editor.apply()

                    // 메인 화면으로 이동
                    val intent = Intent(requireContext(), AfterLoginHomeActivity::class.java)
                    intent.putExtra("userEmail", user.id)  // 필요시 이메일을 넘길 수 있습니다
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
