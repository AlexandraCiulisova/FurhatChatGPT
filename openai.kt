package furhatos.app.openaiskill.flow.chatbot

import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.SystemMessage
import com.theokanning.openai.completion.chat.UserMessage
import com.theokanning.openai.service.OpenAiService
import furhatos.flow.kotlin.DialogHistory
import furhatos.flow.kotlin.Furhat


/** API Key to GPT3 language model. Get access to the API and generate your key from: https://openai.com/api/ **/
val serviceKey = ""

class OpenAIChatbot(val description: String, val userName: String, val agentName: String) {

    val service = OpenAiService(serviceKey)

    fun getNextResponse(): String {
        /** The prompt for the chatbot includes a context of ten "lines" of dialogue. **/
        val history = Furhat.dialogHistory.all.takeLast(50).mapNotNull {
            when (it) {
                is DialogHistory.ResponseItem -> {
                    "$userName: ${it.response.text}"
                }
                is DialogHistory.UtteranceItem -> {
                    "$agentName: ${it.toText()}"
                }
                else -> null
            }
        }.joinToString(separator = "\n")


        // Create a list of ChatMessage objects
        val messages: MutableList<ChatMessage> = ArrayList()
        messages.add(SystemMessage("Y$description\\n\\n$history\\n$agentName:"))

        val ChatCompletionRequest = ChatCompletionRequest.builder()
            .model("gpt-4o-mini")
            .messages(messages)
            .n(1)
            .maxTokens(50)
            .build()
        try {
            val completion = service.createChatCompletion(ChatCompletionRequest)
            val response = completion.getChoices().get(0).getMessage().getContent()
            return response
        }catch (e: Exception) {
            println("Problem with connection to OpenAI: " + e.message)
        }
        return "I am having problems connecting to OpenAI"

    }

}

