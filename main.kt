package furhatos.app.openaiskill

import furhatos.app.openaiskill.flow.Init
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class OpenaiskillSkill : Skill() {
    override fun start() {
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
