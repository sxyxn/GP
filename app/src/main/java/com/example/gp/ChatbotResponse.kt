import com.google.gson.annotations.SerializedName

class ChatbotResponse {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("completion")
    var completion: String? = null
}
