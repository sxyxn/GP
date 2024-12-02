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
           
        5. 흰여울 문화마을
            주소: 부산광역시 영도구 영선동4가 1043
            설명: 재개발된 예술적인 문화마을로 좁은 골목, 거리벽화, 해안 산책로가 있는 곳입니다.
            관광 목적: 사진 촬영, 벽화 구경 등
            
        6. 태종대 유원지
            주소: 부산광역시 영도구 전망로 24
            설명: 절벽 위의 공원으로 바다를 조망할 수 있는 전망대, 열차, 등대 등이 있습니다.
            관광 목적: 공원 산책, 해안가 구경, 사진 촬영, 열차 탑승 등
            
        7. 용두산 공원
            주소: 부산광역시 중구 용두산길 37-55
            설명: 129m 높이의 전망대, 동상, 석조 기념물로 유명한 산림 공원입니다.
            관광 목적: 전망대 경험, 사진 촬영, 남포동 구경 등
            
        8. 부산 엑스 더 스카이
            주소: 부산광역시 해운대구 달맞이길 30
            설명: 해운대의 오션뷰와 부산의 화려한 시티뷰를 볼 수 있는 전망대 입니다.
            관광 목적: 휴식, 전망대 경험, 사진 촬영, 부산의 시티뷰 구경 등
            
        9. 범어사
            주소: 부산광역시 금정구 범어사로 250
            설명: 서기 678년에 의상 법사가 세운 한국의 화엄종 불교 사찰입니다.
            관광 목적: 부산의 과거 사찰 관광, 사진 촬영 등
            
        10. 씨라이프 부산 아쿠아리움
            주소: 부산광역시 해운대구 해운대해변로 266
            설명: 가족이 함께 즐길 수 있도록 다양한 해양 동물을 전시하며 대부분 놀이 공간과 그룹 패키지가 있습니다.
            관광 목적: 해양생물 체험 등
            
        11. 오륙도 스카이워크
            주소: 부산광역시 남구 오륙도로 137
            설명: 무료로 이용할 수 있는 캔틸레버식 다리로, 유리 바닥에서 남해와 오륙도의 풍경을 감상할 수 있습니다.
            관광 목적: 오륙도의 풍경 감상, 사진 촬영 등
            
        12. 롯데월드 어드벤처 부산
            주소: 부산광역시 기장군 동부산관광로 42
            설명: 놀이동산으로 가족들, 연인들, 친구들과 함께 즐길 수 있는 테마파크입니다.
            관광 목적: 놀이기구를 체험, 사진 촬영 등
            
        13. 국제시장
            주소: 부산광역시 중구 중구로 지하31
            설명: 길거리 음식, 가정용품, 기념품 등을 판매하는 매장이 넓게 펼쳐진 활기 넘치는 시장입니다.
            관광 목적: 먹거리, 볼거리, 즐길거리를 체험할 수 있는 전총 시장 구경
            
        14. 부산 시민공원
            주소: 부산광역시 부산진구 시민공원로 73
            설명: 한때 미군 기지였던 자리에 위치한 도심 속 대형 공원으로 산책로, 놀이터, 춤추는 분수가 있습니다.
            관광 목적: 도심 속 공원 체험, 산책, 피크닉, 사진 촬영 등
            
        15. 스파랜드 신세계백화점 센텀시티점
            주소: 부산광역시 해운대구 센텀남대로 35
            설명: 신세계 백화점 내에 위치한 스파랜드입니다.
            관광 목적: 찜질방 체험, 먹거리, 사진 촬영 등
            
        16. 송도해상캐이블카 송도 스카이파크
            주소: 부산광역시 서구 암남공원로 181
            설명: 케이블카를 탑승하여 송도를 구경할 수 있는 스카이 파크 입니다.
            관광 목적: 부산 송도 관광, 사진 촬영 등
            
        17. BIFF 광장
            주소: 부산광역시 중구 구덕로 58-1
            설명: 개봉관이 모여 있는 번화한 지역으로 길거리 음식을 즐기며 한국 유명 배우의 핸드프린트를 볼 수 있습니다.
            관광 목적: 영화 관련 체험 및 관광, 사진 촬영, 먹거리 등
            
        18. 유엔기념공원
            주소: 부산광역시 남구 유엔평화로 93
            설명: 한국 전쟁 중 사망한 군인들을 위한 국제 연합 묘지로 여러 국가의 국기와 이름이 새겨진 벽이 있습니다.
            관광 목적: 한국 전쟁 역사 이해
            
        19. 해동 용궁사
            주소: 부산광역시 기장군 용궁길 86
            설명: 광활한 바다를 마주한 오래된 불교 사찰로 14세기에 세워졌습니다.
            관광 목적: 바다와 마주한 사찰 관광, 사진 촬영 등
            
        20. 해운대 달맞이길
            주소: 부산광역시 해운대구 중2동
            설명: 부산에서 유명한 드라이브 코스로 다양한 먹거리와 바다를 구경할 수 있습니다.
            관광 목적: 먹거리, 드라이브, 사진 촬영 등
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
