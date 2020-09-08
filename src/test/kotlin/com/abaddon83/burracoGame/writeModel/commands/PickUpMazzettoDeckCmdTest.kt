package com.abaddon83.burracoGame.writeModel.commands

import com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories.EventStoreInMemoryAdapter
import com.abaddon83.burracoGame.writeModel.events.*
import com.abaddon83.burracoGame.writeModel.models.BurracoDeck
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.BurracoTris
import com.abaddon83.burracoGame.writeModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.decks.ListCardsBuilder
import com.abaddon83.burracoGame.writeModel.models.decks.Ranks
import com.abaddon83.burracoGame.writeModel.models.decks.Suits
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.functionals.Invalid
import com.abaddon83.utils.functionals.Valid
import org.junit.Before
import org.junit.Test

class PickUpMazzettoDeckCmdTest {

    @Before
    fun loadEvents(){
        eventStore.save(events)
    }

    @Test
    fun `Given a command to pick up a mazzetto, when I execute the command, then the mazzetto is picked up`() {
        val command = PickUpMazzettoDeckCmd(gameIdentity = gameIdentity, playerIdentity = playerIdentity1)
        assert(commandHandler.handle(command) is Valid)
    }

    @Test
    fun `Given a command to pick up a mazzetto already picked up, when I execute the command, then I receive an error`() {
        val command = PickUpMazzettoDeckCmd(gameIdentity = gameIdentity, playerIdentity = playerIdentity1)
        assert(commandHandler.handle(command) is Valid)
        assert(commandHandler.handle(command) is Invalid)
    }

    @Test
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`() {
        val command = PickUpMazzettoDeckCmd(gameIdentity = GameIdentity.create(), playerIdentity = playerIdentity1)
        assert(commandHandler.handle(command) is Invalid)
    }

    val eventStore = EventStoreInMemoryAdapter()
    private val commandHandler = CommandHandler(eventStore)
    val deck = BurracoDeck.create()
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
            BurracoGameCreated( identity = gameIdentity, deck = deck.cards),
            PlayerAdded(identity = gameIdentity, playerIdentity = playerIdentity1),
            PlayerAdded(identity = gameIdentity, playerIdentity = playerIdentity2),
            GameStarted(
                    identity = gameIdentity,
                    players = playersCards,
                    deck = burracoDeckCards,
                    mazzettoDeck1 = mazzettoDeck1Cards,
                    mazzettoDeck2 = mazzettoDeck2Cards,
                    discardPileCards = discardPileCards,
                    playerTurn = playerIdentity1
            ),
            CardsPickedFromDiscardPile(identity = gameIdentity, playerIdentity = playerIdentity1, cards = discardPileCards),
            TrisDropped(identity = gameIdentity, playerIdentity = playerIdentity1, tris = burracoTris.copy(cards = burracoTris.showCards().plus(discardPileCards)))
            //CardDroppedIntoDiscardPile(gameIdentity = gameIdentity, player = playerIdentity1, cardDropped = discardPileCards.first())

    )
}