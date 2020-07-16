package com.abaddon83.burracoGame.adapters.burracoGameRepositoryAdapters.ev

import com.abaddon83.burracoGame.commands.StartGameCmd
import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.BurracoGameCreated
import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.*
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.GameStarted
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.PlayerAdded
import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.decks.ListCardsBuilder
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.InMemoryEventStore
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import org.junit.Test

class BurracoGameRepositoryEVAdapterTest {

    @Test
    fun `load an event`() {
        val eventBus = AsyncInMemoryBus(GlobalScope)
        val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
        val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)

        val gameIdentity = GameIdentity.create()

        val playerIdentity1 = PlayerIdentity.create()
        val playerIdentity2 = PlayerIdentity.create()

        val allCards = ListCardsBuilder.allRanksWithJollyCards()
                .plus(ListCardsBuilder.allRanksWithJollyCards())
                .shuffled()
        val cardsPlayer1 = Pair(playerIdentity1,allCards.take(11))
        val cardsPlayer2 = Pair(playerIdentity2,allCards.take(11))

        val mazzettoDeck1Cards = allCards.take(11)
        val mazzettoDeck2Cards = allCards.take(11)

        val discardPileCards = allCards.take(1)

        val burracoDeckCards = allCards.take(108-1-11-11-11-11)

        val playersCards = mapOf<PlayerIdentity,List<Card>>(cardsPlayer1,cardsPlayer2)
        val aggregate = BurracoGame(identity = gameIdentity)
        val events = listOf<Event>(
                BurracoGameCreated(gameIdentity = gameIdentity, players = listOf()),
                PlayerAdded(gameIdentity = gameIdentity, burracoPlayer = PlayerNotAssigned(playerIdentity1)),
                PlayerAdded(gameIdentity = gameIdentity, burracoPlayer = PlayerNotAssigned(playerIdentity2)),
                GameStarted(
                        gameIdentity = gameIdentity,
                        playersCards = playersCards,
                        burracoDeckCards = burracoDeckCards,
                        mazzettoDeck1Cards = mazzettoDeck1Cards,
                        mazzettoDeck2Cards = mazzettoDeck2Cards,
                        discardPileCards = discardPileCards,
                        playerTurn = playerIdentity1
                ),
                CardPickedFromDeck(gameIdentity = gameIdentity,player = playerIdentity1, cardTaken = burracoDeckCards[0] ),
                CardDroppedIntoDiscardPile(gameIdentity = gameIdentity, player = playerIdentity1, cardDropped = burracoDeckCards[0]),
                TurnEnded(gameIdentity = gameIdentity, player = playerIdentity1, nextPlayerTurn = playerIdentity2)


        )
        eventStore.uploadEvents(aggregate, events)

        val burracoGameLoaded = repository.getById(gameIdentity)

        assert(burracoGameLoaded.identity() == gameIdentity)
        assert(burracoGameLoaded.players.size == 2)
        assert((burracoGameLoaded as BurracoGameExecution).playerCards(playerIdentity1).size == 11)
        assert((burracoGameLoaded as BurracoGameExecution).showPlayerTurn() == playerIdentity2)


        val classType = when (burracoGameLoaded) {
            is BurracoGameWaitingPlayers -> "BurracoGameWaitingPlayers"
            is BurracoGameExecutionTurnBeginning -> "BurracoGameExecutionTurnBeginning"
            is BurracoGameExecutionTurnExecution -> "BurracoGameExecutionTurnExecution"
            is BurracoGameExecutionTurnEnd -> "BurracoGameExecutionTurnEnd"
            is BurracoGameEnded -> "BurracoGameEnded"
            else -> "boh :("
        }
        println()
        println("classType: $classType")
        println()
    }
}