package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
import com.abaddon83.burracoGame.commandModel.models.BurracoScale
import com.abaddon83.burracoGame.commandModel.models.BurracoTris
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.CardDroppedIntoDiscardPile
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.CardPickedFromDeck
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.CardsPickedFromDiscardPile
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.TrisDropped
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.GameStarted
import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.PlayerAdded
import com.abaddon83.burracoGame.commandModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.ListCardsBuilder
import com.abaddon83.burracoGame.commandModel.models.decks.Ranks
import com.abaddon83.burracoGame.commandModel.models.decks.Suits
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

class PickUpMazzettoDeckCmdTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testAdapters)
    }

    @Test
    fun `(async) Given a command to pick up a mazzetto, when I execute the command, then the mazzetto is picked up`() {
        val command = PickUpMazzettoDeckCmd(gameIdentity = gameIdentity, playerIdentity = playerIdentity1)
        runBlocking { PickUpMazzettoDeckHandler().handleAsync(command) }
    }

    @Test
    fun `Given a command to pick up a mazzetto, when I execute the command, then the mazzetto is picked up`() {
        val command = PickUpMazzettoDeckCmd(gameIdentity = gameIdentity, playerIdentity = playerIdentity1)
        PickUpMazzettoDeckHandler().handle(command)
    }

    @Test
    fun `Given a command to pick up a mazzetto already picked up, when I execute the command, then I receive an error`() {
        val command = PickUpMazzettoDeckCmd(gameIdentity = gameIdentity, playerIdentity = playerIdentity1)
        assertFailsWith(IllegalStateException::class) {
            PickUpMazzettoDeckHandler().handle(command)
            PickUpMazzettoDeckHandler().handle(command)
        }
    }

    @Test
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`() {
        val command = PickUpMazzettoDeckCmd(gameIdentity = GameIdentity.create(), playerIdentity = playerIdentity1)
        assertFailsWith(IllegalStateException::class) {
            PickUpMazzettoDeckHandler().handle(command)
        }
    }

    val gameIdentity: GameIdentity = GameIdentity.create()
    val aggregate = BurracoGame(identity = gameIdentity)
    val playerIdentity1 = PlayerIdentity.create()
    val playerIdentity2 = PlayerIdentity.create()

    val allCards = ListCardsBuilder.allRanksWithJollyCards()
            .plus(ListCardsBuilder.allRanksWithJollyCards())
            .shuffled()

    val burracoTris = BurracoTris(
            identity = BurracoIdentity.create(),
            rank = Ranks.Five,
            cards = listOf(Card(Suits.Tile, rank = Ranks.Five), Card(Suits.Heart, rank = Ranks.Five), Card(Suits.Tile, rank = Ranks.Five)))

    val cardsPlayer1 = Pair(playerIdentity1, burracoTris.showCards())
    val cardsPlayer2 = Pair(playerIdentity2, allCards.take(11))

    val mazzettoDeck1Cards = allCards.take(11)
    val mazzettoDeck2Cards = allCards.take(11)

    val discardPileCards = listOf(Card(Suits.Pike, rank = Ranks.Five))

    val burracoDeckCards = allCards.take(108 - 1 - 3 - 11 - 11 - 11)

    val playersCards = mapOf<PlayerIdentity, List<Card>>(cardsPlayer1, cardsPlayer2)

    val events = listOf<Event>(
            BurracoGameCreated.create(gameIdentity = gameIdentity),
            PlayerAdded.create(gameIdentity = gameIdentity, playerIdentity = playerIdentity1),
            PlayerAdded.create(gameIdentity = gameIdentity, playerIdentity = playerIdentity2),
            GameStarted.create(
                    gameIdentity = gameIdentity,
                    playersCards = playersCards,
                    burracoDeckCards = burracoDeckCards,
                    mazzettoDeck1Cards = mazzettoDeck1Cards,
                    mazzettoDeck2Cards = mazzettoDeck2Cards,
                    discardPileCards = discardPileCards,
                    playerTurn = playerIdentity1
            ),
            CardsPickedFromDiscardPile(gameIdentity = gameIdentity, player = playerIdentity1, cardsTaken = discardPileCards),
            TrisDropped(gameIdentity = gameIdentity, player = playerIdentity1, tris = burracoTris.copy(cards = burracoTris.showCards().plus(discardPileCards)))
            //CardDroppedIntoDiscardPile(gameIdentity = gameIdentity, player = playerIdentity1, cardDropped = discardPileCards.first())

    )

    val testAdapters = module {
        val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
        val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
        eventStore.uploadEvents(aggregate, events)

        val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)
        single<BurracoGameRepositoryPort> { repository }
    }
}