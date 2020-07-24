package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.CardPickedFromDeck
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.GameStarted
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.PlayerAdded
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.ListCardsBuilder
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.testUtils.DIRepositoryTestRule
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.eventStore.inMemory.InMemoryEventStore
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.assertFailsWith

class DropCardOnDiscardPileCmdTest: KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testAdapters)
    }

    @Test
    fun `(async) Given a command to drop a card in the discard pile, when I execute the command, then the card is dropped`(){
        val command = DropCardOnDiscardPileCmd(gameIdentity = gameIdentity,playerIdentity = playerIdentity1, card = burracoDeckCards[0])
        runBlocking { DropCardOnDiscardPileHandler().handleAsync(command) }
    }

    @Test
    fun `Given a command to drop a card in the discard pile, when I execute the command, then the card is dropped`(){
        val command = DropCardOnDiscardPileCmd(gameIdentity = gameIdentity,playerIdentity = playerIdentity1, card = burracoDeckCards[0])
        DropCardOnDiscardPileHandler().handle(command)
    }

    @Test
    fun `Given a command to drop a card already dropped, when I execute the command, then nothing happened`(){
        val command = DropCardOnDiscardPileCmd(gameIdentity = gameIdentity,playerIdentity = playerIdentity1, card = burracoDeckCards[0])
        DropCardOnDiscardPileHandler().handle(command)
        DropCardOnDiscardPileHandler().handle(command)
    }

    @Test
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`(){
        val command = DropCardOnDiscardPileCmd(gameIdentity = GameIdentity.create(),playerIdentity = playerIdentity1, card = burracoDeckCards[0])
        assertFailsWith(IllegalStateException::class) {
            DropCardOnDiscardPileHandler().handle(command)
        }
    }

    val gameIdentity: GameIdentity = GameIdentity.create()
    val aggregate = BurracoGame(identity = gameIdentity)
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

    val events = listOf<Event>(
            BurracoGameCreated.create(gameIdentity = gameIdentity),
            PlayerAdded.create(gameIdentity = gameIdentity, playerIdentity = playerIdentity1),
            PlayerAdded.create(gameIdentity = gameIdentity, playerIdentity = playerIdentity2),
            GameStarted(
                    gameIdentity = gameIdentity,
                    playersCards = playersCards,
                    burracoDeckCards = burracoDeckCards,
                    mazzettoDeck1Cards = mazzettoDeck1Cards,
                    mazzettoDeck2Cards = mazzettoDeck2Cards,
                    discardPileCards = discardPileCards,
                    playerTurn = playerIdentity1
            ),
            CardPickedFromDeck(gameIdentity = gameIdentity, player = playerIdentity1, cardTaken = burracoDeckCards[0])
    )

    val testAdapters = module {
        val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
        val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
        eventStore.uploadEvents(aggregate,events)

        val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)
        single< BurracoGameRepositoryPort> { repository}
    }
}