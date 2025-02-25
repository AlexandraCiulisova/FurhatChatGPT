package furhatos.app.openaiskill.flow

import furhatos.app.openaiskill.flow.main.Idle
import furhatos.app.openaiskill.flow.main.Greeting
import furhatos.app.openaiskill.setting.DISTANCE_TO_ENGAGE
import furhatos.app.openaiskill.setting.MAX_NUMBER_OF_USERS
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users

val Init: State = state {
    init {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)
        furhat.param.endSilTimeout =  800 //milliseconds 800
        furhat.param.noSpeechTimeout = 5000 //milliseconds 5000
    }
    onEntry {
        /** start interaction */
        when {
            furhat.isVirtual() -> goto(Idle) // Convenient to bypass the need for user when running Virtual Furhat
            users.hasAny() -> {
                furhat.attend(users.random)
                goto(Greeting)
            }
            else -> goto(Idle)
        }
    }

}
