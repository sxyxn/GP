import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatbotService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer YOUR_API_KEY"
    )
    @POST("completion")
    fun getCompletion(@Body request: ChatbotRequest): Call<ChatbotResponse>
}
