package com.abaddon83.burracoGame.commandModel.commands

import com.abaddon83.burracoGame.commandModel.adapters.burracoGameRepositoryAdapters.BurracoGameRepositoryEVAdapter
import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
import com.abaddon83.burracoGame.commandModel.models.BurracoScale
import com.abaddon83.burracoGame.commandModel.models.MazzettoDeck
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.*
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
import com.abaddon83.utils.es.Event
import com.abaddon83.utils.es.eventStore.inMemory.InMemoryEventStore
import eventsourcing.messagebus.AsyncInMemoryBus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.assertFailsWith

class EndGameCmdTest: KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testAdapters)
    }

    @Test
    fun `(async) Given a command to end game, when I execute the command, then the game is ended`(){
        val command = EndGameCmd(gameIdentity = gameIdentity,playerIdentity = playerIdentity1)
        runBlocking { EndGameHandler().handleAsync(command) }
    }

    @Test
    fun `Given a command to end game, when I execute the command, then the game is ended`(){
        val command = EndGameCmd(gameIdentity = gameIdentity,playerIdentity = playerIdentity1)
        EndGameHandler().handle(command)
    }

    @Test
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`(){
        val command = EndGameCmd(gameIdentity = GameIdentity.create(),playerIdentity = playerIdentity1)
        assertFailsWith(IllegalStateException::class) {
            EndGameHandler().handle(command)
        }
    }

    val gameIdentity: GameIdentity = GameIdentity.create()
    val aggregate = BurracoGame(identity = gameIdentity)
    val playerIdentity1 = PlayerIdentity.create()
    val playerIdentity2 = PlayerIdentity.create()

    val allCards = ListCardsBuilder.allRanksWithJollyCards()
            .plus(ListCardsBuilder.allRanksWithJollyCards())
            .shuffled()
    val burracoScale = BurracoScale(
            identity = BurracoIdentity.create(),
            suit = Suits.Tile,
            cards = listOf(
                    Card(Suits.Tile,rank = Ranks.Three),
                    Card(Suits.Tile,rank = Ranks.Four),
                    Card(Suits.Tile,rank = Ranks.Five),
                    Card(Suits.Tile,rank = Ranks.Six),
                    Card(Suits.Tile,rank = Ranks.Seven),
                    Card(Suits.Tile,rank = Ranks.Eight),
                    Card(Suits.Tile,rank = Ranks.Nine),
                    Card(Suits.Tile,rank = Ranks.Ten),
                    Card(Suits.Tile,rank = Ranks.Jack),
                    Card(Suits.Tile,rank = Ranks.Queen),
                    Card(Suits.Tile,rank = Ranks.King),
                    Card(Suits.Tile,rank = Ranks.Ace)

            )
    )

    val burracoScale2 = BurracoScale(
            identity = BurracoIdentity.create(),
            suit = Suits.Tile,
            cards = listOf(
                    Card(Suits.Pike,rank = Ranks.Three),
                    Card(Suits.Pike,rank = Ranks.Four),
                    Card(Suits.Pike,rank = Ranks.Five),
                    Card(Suits.Pike,rank = Ranks.Six),
                    Card(Suits.Pike,rank = Ranks.Seven),
                    Card(Suits.Pike,rank = Ranks.Eight),
                    Card(Suits.Pike,rank = Ranks.Nine),
                    Card(Suits.Pike,rank = Ranks.Ten),
                    Card(Suits.Pike,rank = Ranks.Jack),
                    Card(Suits.Pike,rank = Ranks.Queen)
            )
    )

    val cardsPlayer1 = Pair(playerIdentity1,burracoScale.showCards().take(11))
    val cardsPlayer2 = Pair(playerIdentity2,allCards.take(11))

    val mazzettoDeck1Cards = burracoScale2.showCards().plus(Card(Suits.Clover,Ranks.Five))
    val mazzettoDeck2Cards = allCards.take(11)

    val discardPileCards = burracoScale.showCards().takeLast(1)
    val burracoDeckNumCard = 108 -  mazzettoDeck1Cards.size - mazzettoDeck2Cards.size - cardsPlayer1.second.size - cardsPlayer2.second.size - discardPileCards.size
    val burracoDeckCards = allCards.take(burracoDeckNumCard)

    val playersCards = mapOf<PlayerIdentity,List<Card>>(cardsPlayer1,cardsPlayer2)

    val events = listOf<Event>(
            BurracoGameCreated(gameIdentity = gameIdentity),
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
            CardsPickedFromDiscardPile(gameIdentity = gameIdentity, player = playerIdentity1, cardsTaken = discardPileCards),
            ScaleDropped(gameIdentity= gameIdentity, player = playerIdentity1, scale = burracoScale),
            MazzettoPickedUp(gameIdentity = gameIdentity, player = playerIdentity1, mazzettoDeck = MazzettoDeck.create(mazzettoDeck1Cards)),
            ScaleDropped(gameIdentity= gameIdentity, player = playerIdentity1, scale = burracoScale2),
            CardDroppedIntoDiscardPile(gameIdentity= gameIdentity, player = playerIdentity1,cardDropped = Card(Suits.Clover,Ranks.Five))
    )

    val testAdapters = module {
        val eventBus = AsyncInMemoryBus(GlobalScope)//.register(burracoGameListProjection)
        val eventStore = InMemoryEventStore<GameIdentity>(eventBus)
        eventStore.uploadEvents(aggregate,events)

        val repository = BurracoGameRepositoryEVAdapter(eventStore = eventStore)
        single< BurracoGameRepositoryPort> { repository}
    }
}