package com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.BurracoPlayer
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

data class BurracoGameWaitingPlayers constructor(
        override val identity: GameIdentity,
        override val players: List<BurracoPlayer>
) : BurracoGame() {

    fun addPlayer(player: BurracoPlayer): BurracoGameWaitingPlayers {
        assert(players.size < maxPlayers){"Maximum number of players reached, (Max: ${maxPlayers})"}
        assert(!isAlreadyAPlayer(player.identity())) {"The player ${player.identity()} is already a player of game ${this.identity()}"}

        return BurracoGameWaitingPlayers(identity,listOf(players, listOf(player)).flatten())
    }

    fun isAlreadyAPlayer(playerIdentity: PlayerIdentity): Boolean {
        return players.find { p -> p.identity() == playerIdentity } != null
    }

    fun start(): BurracoGameExecutionTurnBeginning {
        assert(players.size >1) {"Not enough players to initiate the game, ( Min: ${minPlayers})"}
        val burracoDealer = BurracoDealer.create(this)
        return BurracoGameExecutionTurnBeginning.create(this,burracoDealer)

    }

}