package furhatos.app.openaiskill.flow.main

import furhatos.app.openaiskill.flow.Parent
import furhatos.app.openaiskill.flow.chatbot.OpenAIChatbot
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.Goodbye
import furhatos.nlu.common.Yes
import furhatos.nlu.common.No

val Greeting: State = state(Parent) {
    onEntry {
        delay(10000)
        furhat.say("Hi there! I am Furhat and I will be your company for today. I hope you are just as excited as I am!")
        furhat.ask("Are you ready to play? Please respond with yes or no. Actually, just say yes! Just kidding!")
    }

    onResponse<Yes> {
        furhat.say("Excellent choice!")
        goto(Start)
    }

    onResponse<No> {
        furhat.say("You are a sneaky one. I gave you only an illusion of choice. I wish you the best of luck.")
        goto(Start)
    }

    onResponse<Goodbye> {
        furhat.say("Goodbye")
        goto(Idle)
    }

    onNoResponse {
        furhat.ask("Sorry, I didn't hear anything")
    }
}
