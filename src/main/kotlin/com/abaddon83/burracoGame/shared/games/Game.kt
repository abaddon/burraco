package com.abaddon83.burracoGame.shared.games

import com.abaddon83.burracoGame.shared.players.Player
import com.abaddon83.utils.ddd.Entity

abstract class Game: Entity<GameIdentity>() {
    abstract val maxPlayers: Int
    abstract val minPlayers: Int
    abstract val totalCardsRequired: Int
    protected abstract val players: List<Player>

    fun numPlayers(): Int = players.size

    open fun listOfPlayers(): List<Player> = players

}
