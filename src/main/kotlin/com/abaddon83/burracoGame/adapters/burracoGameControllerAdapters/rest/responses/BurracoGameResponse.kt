package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.responses

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded

data class BurracoGameResponse(
        val identity: GameIdentityResponse,
        val status: String,
        val players: List<BurracoPlayerResponse>

) {
    constructor(game: BurracoGame): this(
                identity = GameIdentityResponse(game.identity()),
                status = fillStatus(game),
                players = game.listOfPlayers().map { BurracoPlayerResponse(it) }
        )

    constructor(game: BurracoGameWaitingPlayers): this(
            identity = GameIdentityResponse(game.identity()),
            status = fillStatus(game),
            players = game.listOfPlayers().map { BurracoPlayerResponse(it) }
    )

    companion object Factory{
        private fun fillStatus(game: BurracoGame): String =
                when(game){
                    is BurracoGameWaitingPlayers -> "WAITING_PLAYERS"
                    is BurracoGameExecution -> "EXECUTION"
                    is BurracoGameEnded -> "ENDED"
                    else -> "UNKNOWN"
                }
    }

}