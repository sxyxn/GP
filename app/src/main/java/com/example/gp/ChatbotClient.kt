import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatbotClient {
    private val BASE_URL = "https://api.openai.com/v1/"
    private val service: ChatbotService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ChatbotService::class.java)
    }

    fun getChatResponse(userInput: String, callback: Callback<ChatbotResponse>) {
        // Usage example
        val request = ChatbotRequest().apply {
            this.userInput = userInput // `this` 키워드를 사용하여 클래스의 프로퍼티에 접근
        }

        val call = service.getCompletion(request)
        call.enqueue(callback)
    }

}
