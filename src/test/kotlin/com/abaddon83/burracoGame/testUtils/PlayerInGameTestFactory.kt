package com.abaddon83.burracoGame.testUtils

import com.abaddon83.burracoGame.writeModel.models.BurracoScale
import com.abaddon83.burracoGame.writeModel.models.BurracoTris
import com.abaddon83.burracoGame.writeModel.models.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity

data class PlayerInGameTestFactory(val playerInGame: PlayerInGame) {

    fun withCards(cards: List<Card>): PlayerInGameTestFactory =
            this.copy(playerInGame = playerInGame.copy(cards = cards))

    fun withTrisOnTable(burracoTris: BurracoTris): PlayerInGameTestFactory =
            this.copy(playerInGame = playerInGame.dropATris(burracoTris))


    fun withScalaOnTable(burracoScale: BurracoScale): PlayerInGameTestFactory =
            this.copy(playerInGame = playerInGame.dropAScale(burracoScale))

    fun build(): PlayerInGame = playerInGame

    companion object Factory {
        fun create(): PlayerInGameTestFactory = create(PlayerIdentity.create())
        fun create(playerIdentity: PlayerIdentity): PlayerInGameTestFactory =
                PlayerInGameTestFactory(playerInGame = PlayerInGame.create(playerIdentity, listOf()))
    }
}