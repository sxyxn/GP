import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatResponseCallback : Callback<ChatbotResponse> {
    override fun onResponse(call: Call<ChatbotResponse>, response: Response<ChatbotResponse>) {
        if (response.isSuccessful) {
            val chatbotResponse = response.body()
            // API 응답을 처리합니다.
        } else {
            // API 호출이 실패한 경우 에러 처리를 합니다.
        }
    }

    override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
        // API 호출이 실패한 경우 에러 처리를 합니다.
    }
}
