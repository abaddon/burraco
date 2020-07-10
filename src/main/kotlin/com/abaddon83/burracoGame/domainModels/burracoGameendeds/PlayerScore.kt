package com.abaddon83.burracoGame.domainModels.burracoGameendeds

import com.abaddon83.burracoGame.domainModels.BurracoPlayer
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.burracos.Burraco
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

data class PlayerScore private constructor(
        override val identity: PlayerIdentity,
        val winner: Boolean,
        val burracoList: List<BurracoPoint>,
        val remainedCards: List<Card>
): BurracoPlayer() {

    companion object Factory{
        fun create(player: PlayerInGame, winner: Boolean): PlayerScore =
            PlayerScore(
                    identity = player.identity(),
                    winner = winner,
                    burracoList = burracoList(player.burracoList()),
                    remainedCards = player.showMyCards()
            )

        private fun burracoList(burracoList: List<Burraco>): List<BurracoPoint> =
            burracoList.map { burraco ->
                BurracoPoint(burraco)
            }

    }

}