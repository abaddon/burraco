package com.abaddon83.burracoGame.writeModel.models.games

import com.abaddon83.burracoGame.writeModel.models.decks.Deck
import com.abaddon83.burracoGame.writeModel.models.players.Player

interface Game{
    val maxPlayers: Int
    val minPlayers: Int
    val totalCardsRequired: Int
    val players: List<Player>
    val deck: Deck
    fun numPlayers(): Int = players.size
    fun listOfPlayers(): List<Player> = players


}
