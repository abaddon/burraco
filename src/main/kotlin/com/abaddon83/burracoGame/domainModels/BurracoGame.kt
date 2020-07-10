package com.abaddon83.burracoGame.domainModels

import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.shared.games.Game
import com.abaddon83.burracoGame.shared.games.GameIdentity

abstract class BurracoGame: Game() {
    override val maxPlayers: Int = 4
    override val minPlayers: Int = 2
    override val totalCardsRequired: Int = 108
    abstract override val players: List<BurracoPlayer>

    override fun listOfPlayers(): List<BurracoPlayer> = players

    companion object Factory {
        fun create(gameIdentity: GameIdentity): BurracoGameWaitingPlayers {
            return BurracoGameWaitingPlayers(gameIdentity,listOf())
        }
    }
}