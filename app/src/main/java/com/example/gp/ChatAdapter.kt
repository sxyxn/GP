package com.example.gp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ChatMessage(
    val message: String, // 메시지 내용
    val isUser: Boolean, // 사용자가 보낸 메시지인지 여부
    val isButton: Boolean = false // 버튼 관련 필드 (기본값은 false)
)

class ChatAdapter(private val messages: MutableList<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_TEXT = 0
    private val VIEW_TYPE_BUTTON = 1

    // 클릭 리스너 인터페이스
    private var onButtonClickListener: ((String) -> Unit)? = null

    // 리스너 설정 메서드
    fun setOnButtonClickListener(listener: (String) -> Unit) {
        onButtonClickListener = listener
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.addPlaceButton)  // 버튼 ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TEXT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chatbot_message, parent, false)
                ChatViewHolder(view)
            }
            VIEW_TYPE_BUTTON -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chatbot_button, parent, false)
                ButtonViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = messages[position]

        when (holder) {
            is ChatViewHolder -> {
                holder.messageTextView.text = chatMessage.message
                holder.messageTextView.textAlignment = if (chatMessage.isUser) View.TEXT_ALIGNMENT_TEXT_END else View.TEXT_ALIGNMENT_TEXT_START
            }
            is ButtonViewHolder -> {
                holder.button.text = "${chatMessage.message} 추가하기"
                holder.button.setOnClickListener {
                    // 버튼 클릭 시 onButtonClickListener 호출
                    onButtonClickListener?.invoke(chatMessage.message)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isButton) VIEW_TYPE_BUTTON else VIEW_TYPE_TEXT
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
