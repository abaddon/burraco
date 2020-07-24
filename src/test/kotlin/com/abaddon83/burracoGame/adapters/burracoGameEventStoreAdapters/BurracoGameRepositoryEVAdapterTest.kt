package com.abaddon83.burracoGame.adapters.burracoGameEventStoreAdapters

import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.*
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.GameStarted
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.PlayerAdded
import com.abaddon83.burracoGame.commandModel.models.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.readModel.models.BurracoGameListProjection
import com.abaddon83.burracoGame.readModel.models.BurracoGameListReadModel
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.ListCardsBuilder
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.eventStore.inMemory.InMemoryEventStore
import com.abaddon83.utils.es.readModel.InMemorySingleDocumentStore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import org.junit.Test

class BurracoGameRepositoryEVAdapterTest {

    @Test
    fun `load an event`() {

        //val burracoGameListDatastore = InMemorySingleDocumentStore<Iterable<com.abaddon83.burracoGame.readModel.models.BurracoGame>>(emptyList())
        //val burracoGameListProjection = BurracoGameListProjection(burracoGameListDatastore)
        //val burracoGameListReadModelFacade = BurracoGameListReadModel(burracoGameListDatastore)

        val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
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
                BurracoGameCreated(identity = gameIdentity.convertTo().toString()),
                PlayerAdded(gameIdentity = gameIdentity, playerIdentity = playerIdentity1),
                PlayerAdded(gameIdentity = gameIdentity, playerIdentity = playerIdentity2),
                GameStarted(
                        gameIdentity = gameIdentity,
                        playersCards = playersCards,
                        burracoDeckCards = burracoDeckCards,
                        mazzettoDeck1Cards = mazzettoDeck1Cards,
                        mazzettoDeck2Cards = mazzettoDeck2Cards,
                        discardPileCards = discardPileCards,
                        playerTurn = playerIdentity1
                ),
                CardPickedFromDeck(gameIdentity = gameIdentity, player = playerIdentity1, cardTaken = burracoDeckCards[0]),
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

        val kMapper = ObjectMapper().registerModule(KotlinModule())
        //println("BurracoGameCreated: ${kMapper.writeValueAsString(events[0])}")
        //println("PlayerAdded1: ${kMapper.writeValueAsString(events[1])}")
        //println("PlayerAdded2: ${kMapper.writeValueAsString(events[2])}")
        //println("GameStarted: ${kMapper.writeValueAsString(events[3])}")
        println("---------")
        //println(burracoGameListReadModelFacade.allBurracoGames())
    }


//    @Test
//    fun `test serialization`() {
//
//
//        val gameIdentity = GameIdentity.create()
//
//        val playerIdentity1 = PlayerIdentity.create()
//
//        val event = PlayerAdded(gameIdentity = gameIdentity, burracoPlayer = PlayerNotAssigned(playerIdentity1))
//
//
//        println(event.burracoPlayer.identity())
//        println()
//        println(serializedRDJ)
//
//    }

}