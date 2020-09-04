package com.abaddon83.burracoGame.writeModel.commands

import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.utils.cqs.commands.Command
import java.lang.Exception

sealed class DomainError(val msg: String)

data class BurracoGameError(val e: String, val burracoGame: BurracoGame): DomainError(e){
    constructor(cmd: Command,exception: Exception, burracoGame: BurracoGame): this("Command ${cmd.javaClass.simpleName} not executed, exception type ${exception.javaClass.simpleName}",burracoGame)
}