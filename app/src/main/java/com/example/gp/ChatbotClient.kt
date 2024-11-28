package com.example.gp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatbotClient {
    private val BASE_URL = "https://api.openai.com/v1/"
    private val service: ChatbotService

    private val messages = mutableListOf<Message>() // 메시지 리스트를 관리

    // 부산 관광지 데이터
    private val touristData = """
        부산 관광지 정보:
        
        1. 광안리 해수욕장
           주소: 부산광역시 수영구 광안해변로 219
           설명: 부산을 대표하는 해수욕장
           관광 목적: 맛집탐방, 야경 등
           
        2. 해운대 해수욕장
           주소: 부산광역시 해운대구 우동
           설명: 가장 유명한 해수욕장 중 하나로, 많은 관광객들이 찾는 곳입니다.
           관광 목적: 해수욕, 맛집 탐방 등
           
        3. 다대포 해수욕장
           주소: 부산광역시 사하구 다대동
           설명: 국내에서 가장 아름다운 일몰을 볼 수 있는 곳 중 하나
           관광 목적: 일몰 명소

        4. 감천 문화 마을
           주소: 부산광역시 사하구 감내2로
           설명: 다채로운 색상의 주택과 벽화가 어우러진 예술적인 마을입니다.
           관광 목적: 사진 촬영, 문화 체험 등
    """.trimIndent()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ChatbotService::class.java)
    }

    fun getChatResponse(userInput: String, callback: Callback<ChatbotResponse>) {
        // 사용자가 입력한 메시지를 메시지 리스트에 추가
        messages.add(Message(role = "user", content = userInput))

        // ChatbotRequest 생성 시 학습 데이터와 사용자 질문을 결합하여 프롬프트로 사용
        val request = ChatbotRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(role = "system", content = "당신은 부산 관광 계획 구성을 도와주는 가이드로서 사용자가 부산의 관광지를 추천해달라고 하면 입력된 관광지 정보 중에서 사용자의 요구에 맞는 관광지를 추천해야 한다." +
                        "답변을 할 때는" +
                        "1. '관광지 이름'" +
                        " - 관광지 주소" +
                        " - 관광지 설명" +
                        " - 추천 활동(맛집 탐방, 일몰 명소 등)" +
                        "위의 양식에 맞춰 추천해야 한다. 입력된 데이터 정보가 없더라도 사용자가 요구하면 그에 관한 관광지 정보도 검색해서 양식에 맞춰 답변해야한다"), // 역할 정보를 추가


                Message(role = "system", content = touristData), // 시스템 메시지로 관광지 정보 추가
                Message(role = "user", content = userInput) // 사용자 입력 메시지
            ),
            max_tokens = 500,
            temperature = 0.7
        )

        val call = service.getCompletion(request)
        call.enqueue(object : Callback<ChatbotResponse> {
            override fun onResponse(call: Call<ChatbotResponse>, response: Response<ChatbotResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val replyContent = it.choices.firstOrNull()?.message?.content ?: "No response"
                        // 챗봇의 응답 메시지를 메시지 리스트에 추가
                        messages.add(Message(role = "assistant", content = replyContent))
                        callback.onResponse(call, response)
                    }
                } else {
                    callback.onFailure(call, Throwable("Error: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
                callback.onFailure(call, t)
            }
        })
    }
}
