package com.abaddon83.burracoGame.commandModel.models.games

import com.abaddon83.burracoGame.commandModel.models.players.Player

interface Game{
    val maxPlayers: Int
    val minPlayers: Int
    val totalCardsRequired: Int
    val players: List<Player>
    fun numPlayers(): Int = players.size
    fun listOfPlayers(): List<Player> = players

}
