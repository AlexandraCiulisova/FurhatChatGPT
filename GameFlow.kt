package furhatos.app.openaiskill.flow.main

import furhatos.flow.kotlin.*
import furhatos.app.openaiskill.flow.chatbot.OpenAIChatbot
import furhatos.app.openaiskill.flow.utils.logUtterance
import furhatos.nlu.common.*
import kotlinx.coroutines.*

val endingWords = listOf("murder", "faith", "glory", "disapproval", "love", "kiss", "anger", "death", "memories", "killing")
var currentWordIndex = 0

fun getRules(): String {
    return "Welcome to the First and Last game! We are going to be crafting an exciting story, so let your creativity run wild!" +
            "Here's how it works: You say a sentence, " +
            "and I'll take the last word to make a new sentence. Then you do the same with my last word. " +
            "Let’s keep it going until we run out of words. "
}

fun getInstructions(): String {
    return "Now we have crafted a story together. But stories do not end here. And not now, but in around 3 minutes. Each of us has a different story. " +
            "You might be curious about mine. But I would first like to hear about yours."
}

val End: State = state {
    onEntry {
        furhat.say("Goodbye! I really enjoyed this experience and I hope you did too.")
    }
}

val Start: State = state {
    onEntry {
        furhat.say(getRules()) // Show the rules before starting
        furhat.ask("Got it? Say Yes or No!")
    }

    onResponse<Yes> {
        furhat.say("Great! Let's have some fun. You will get the game as we go along")
        goto(PlayGame)
    }

    onResponse<No> {
        furhat.say("I will repeat the rules again.")
        furhat.say(getRules())
        furhat.say("You will get the game as we go along. Let's have some fun.")
    }

    onNoResponse {
        furhat.ask("Sorry, I didn't catch that. Are you ready?")
    }
}

val PlayGame: State = state {
    onEntry {
        furhat.ask("It's your turn! Say something.") // Prompt the user
    }

    onResponse { userInput ->
        val userSentence = userInput.text // User's input text
        logUtterance("User", userSentence) // Log user's input

        val lastWord = userSentence.split(" ").last() // Extract the last word

        val prompt = if (currentWordIndex < endingWords.size) {
            val endingWord = endingWords[currentWordIndex]
            currentWordIndex++
            "The user said: '$userSentence'. Respond with a sentence that ends with '$endingWord'. Make the sentence flowy and longer."
        } else {
            "This is the last response in the game."
        }

        // Get OpenAI response
        val aiResponse = call {
            OpenAIChatbot("The following is a conversation between a Human and a Robot. It is a game of first, last, where " +
                    "they take turns and every sentence has to begin with the last word of the previous sentence of the other person. " +
                    "This is the current state of the game $prompt",
                "Robot", "Human")
                .getNextResponse()
        } as String

        if (aiResponse != null) {
            logUtterance("Assistant", aiResponse) // Log assistant's response
            furhat.say(aiResponse)

            if (currentWordIndex < endingWords.size) {
                furhat.listen()
            } else {
                furhat.say("That was the last word. Thanks for playing!")
                goto(Break)
            }
        } else {
            furhat.say("I couldn't generate a response. Let's try again!")
            reentry()
        }
    }

    onNoResponse {
        furhat.say("I didn't hear you. Could you repeat that?")
        reentry()
    }
}

val Break: State = state {
    onEntry {
        furhat.say("Hope that was fun! You were amazing to work with! Let us move onto the next part!")
        furhat.ask("Are you ready?")
    }

    onResponse<Yes> {
        furhat.say("Amazing!")
        goto(Middle)
    }

    onResponse<No> {
        furhat.say("Sorry to hear. I hope that it will satisfy you still.")
        goto(Middle)
    }
}

val Middle: State = state {
    onTime(delay = 3 * 60 * 1000 + 20000) {
        furhat.say("Time's up! Hope to see you soon and have a lovely day!")
        goto(End) // Transition to the End state
    }

    onEntry {
        furhat.say(getInstructions())
        furhat.ask("What’s something new you’ve learned about yourself this year?")
    }

    onResponse { userInput ->
        val userDialogue = userInput.text
        logUtterance("User", userDialogue)

        // Get OpenAI response
        val aiResponseMiddle = call {
            OpenAIChatbot(
                "You are a friendly and engaging AI assistant having a casual conversation with a human user. " +
                        "The conversation is about getting to know each other, and it started with discussing Christmas and New Year's. " +
                        "Feel free to let the topic evolve naturally. The user just said: $userDialogue. " +
                        "Respond conversationally, addressing the user directly without mentioning roles like 'Human' or 'Robot.'",
                "User", "Assistant"
            ).getNextResponse()
        } as String

        if (aiResponseMiddle != null) {
            logUtterance("Assistant", aiResponseMiddle) // Log assistant's response
            furhat.say(aiResponseMiddle)
        } else {
            furhat.say("I couldn't generate a response. Let's try again!")
        }

        furhat.listen() // Restart listening after the pause
    }

    onNoResponse {
        furhat.say("I didn't hear you. Could you repeat that?")
        reentry() // Restart state to prompt the user again
    }
}
