package com.abaddon83.burracoGame.writeModel.commands

import com.abaddon83.burracoGame.writeModel.adapters.eventStoreInMemories.EventStoreInMemoryAdapter
import com.abaddon83.burracoGame.writeModel.events.*
import com.abaddon83.burracoGame.writeModel.models.BurracoDeck
import com.abaddon83.burracoGame.writeModel.models.BurracoGame
import com.abaddon83.burracoGame.writeModel.models.BurracoScale
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

class DropScaleCmdTest {

    @Before
    fun loadEvents(){
        eventStore.save(events)
    }

    val burracoScale = BurracoScale(
            identity = BurracoIdentity.create(),
            suit = Suits.Tile,
            cards = listOf(Card(Suits.Tile,rank = Ranks.Three),Card(Suits.Tile,rank = Ranks.Four),Card(Suits.Tile,rank = Ranks.Five)))

    @Test
    fun ` Given a command to drop a scale, when I execute the command, then the scale is dropped`(){
        val command = DropScaleCmd(gameIdentity = gameIdentity,playerIdentity = playerIdentity1,scale = burracoScale)
        assert(commandHandler.handle(command) is Valid)
    }

    @Test
    fun `Given a command to execute on a burraco game that doesn't exist, when I execute the command, then I receive an error`(){
        val command = DropScaleCmd(gameIdentity = GameIdentity.create(),playerIdentity = playerIdentity1,scale = burracoScale)
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
    val cardsPlayer1 = Pair(playerIdentity1,allCards.take(8).plus(burracoScale.showCards()))
    val cardsPlayer2 = Pair(playerIdentity2,allCards.take(11))

    val mazzettoDeck1Cards = allCards.take(11)
    val mazzettoDeck2Cards = allCards.take(11)

    val discardPileCards = allCards.take(1)

    val burracoDeckCards = allCards.take(108-1-11-11-11-11)

    val playersCards = mapOf<PlayerIdentity,List<Card>>(cardsPlayer1,cardsPlayer2)

    val events = listOf<Event>(
            BurracoGameCreated(identity = gameIdentity, deck = deck.cards),
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
            CardPickedFromDeck(identity = gameIdentity, playerIdentity = playerIdentity1, card = burracoDeckCards[0])
    )
}