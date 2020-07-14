package com.abaddon83.burracoGame.domainModels.burracoGameExecutions

import com.abaddon83.burracoGame.domainModels.BurracoScale
import com.abaddon83.burracoGame.domainModels.BurracoTris
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.BurracoCardsOnTable
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.shared.burracos.BurracoIdentity
import com.abaddon83.burracoGame.shared.decks.Card
import com.abaddon83.burracoGame.shared.decks.Ranks
import com.abaddon83.burracoGame.shared.decks.Suits
import com.abaddon83.burracoGame.shared.players.PlayerIdentity
import com.abaddon83.burracoGame.testUtils.BurracoGameInitTurnTestFactory
import org.junit.Test
import kotlin.test.assertFailsWith

class BurracoGameExecutionTurnExecutionTest {
    
    @Test
fun `Given a player in game, when update the cards order, the operation is executed`() {
        val player1Id = PlayerIdentity.create()
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .buildTurnPhaseExecution()

        val playerCards = game.playerCards(player1Id)
        val orderedCards = playerCards.sorted()

        val actualGame = game.updatePlayerCardsOrder(player1Id, orderedCards)

        assert(actualGame.playerCards(player1Id) == orderedCards)
        assert(actualGame.playerCards(player1Id) != playerCards)
    }

    @Test
fun `Given a player not in game, when update the cards order, I receive an error`() {
        val player1Id = PlayerIdentity.create()
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id).buildTurnPhaseExecution()
        val playerCards = game.playerCards(player1Id)
        val orderedCards = playerCards.sorted()

        assertFailsWith(IllegalStateException::class) {
            game.updatePlayerCardsOrder(PlayerIdentity.create(), orderedCards)
        }
    }


    @Test
fun `Given a player during his turn, when drop a tris, the operation is executed`() {
        val tris = BurracoTris(BurracoIdentity.create(), Ranks.Ace, listOf(Card(Suits.Heart, Ranks.Ace), Card(Suits.Heart, Ranks.Ace), Card(Suits.Clover, Ranks.Ace)))
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, tris.showCards(), BurracoCardsOnTable(listOf(), listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer1(player1)
                .buildTurnPhaseExecution()

        val gameActual = game.dropOnTableATris(player1Id, tris)

        assert(gameActual.playerTrisOnTable(player1Id).size == 1)
        assert(gameActual.playerCards(player1Id).isEmpty())

    }

    @Test
fun `Given a player not in the turn, when drop a tris, I receive an error`() {
        val tris = BurracoTris(BurracoIdentity.create(), Ranks.Ace, listOf(Card(Suits.Heart, Ranks.Ace), Card(Suits.Heart, Ranks.Ace), Card(Suits.Clover, Ranks.Ace)))
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, tris.showCards(), BurracoCardsOnTable(listOf(), listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .setPlayer2Turn()
                .buildTurnPhaseExecution()

        assertFailsWith(IllegalStateException::class) {
            game.dropOnTableATris(player1Id, tris)
        }
    }

    @Test
fun `Given a player not in this game, when drop a tris, I receive an error`() {
        val tris = BurracoTris(BurracoIdentity.create(), Ranks.Ace, listOf(Card(Suits.Heart, Ranks.Ace), Card(Suits.Heart, Ranks.Ace), Card(Suits.Clover, Ranks.Ace)))
        val game = BurracoGameInitTurnTestFactory.create()
                .buildTurnPhaseExecution()

        assertFailsWith(IllegalStateException::class) {
            game.dropOnTableATris(PlayerIdentity.create(), tris)
        }
    }

    @Test
fun `Given a player during his turn, when drop a scale, the operation is executed`() {
        val scale = BurracoScale.create(listOf(
                Card(Suits.Heart,Ranks.Eight),
                Card(Suits.Heart,Ranks.Seven),
                Card(Suits.Heart,Ranks.Six),
                Card(Suits.Heart,Ranks.Five)
        ))
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, scale.showCards(), BurracoCardsOnTable(listOf(), listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer1(player1)
                .buildTurnPhaseExecution()

        val gameActual = game.dropOnTableAScale(player1Id, scale)

        assert(gameActual.playerScalesOnTable(player1Id).size == 1)
        assert(gameActual.playerCards(player1Id).isEmpty())

    }

    @Test
fun `Given a player not in the turn, when drop a scale, I receive an error`() {
        val scale = BurracoScale.create(listOf(
                Card(Suits.Heart,Ranks.Eight),
                Card(Suits.Heart,Ranks.Seven),
                Card(Suits.Heart,Ranks.Six),
                Card(Suits.Heart,Ranks.Five)
        ))
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, scale.showCards(), BurracoCardsOnTable(listOf(), listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer1(player1)
                .setPlayer2Turn()
                .buildTurnPhaseExecution()

        assertFailsWith(UnsupportedOperationException::class) {
            game.dropOnTableAScale(player1Id, scale)
        }
    }

    @Test
fun `Given a player not in this game, when drop a scale, I receive an error`() {
        val scale = BurracoScale.create(listOf(
                Card(Suits.Heart,Ranks.Eight),
                Card(Suits.Heart,Ranks.Seven),
                Card(Suits.Heart,Ranks.Six),
                Card(Suits.Heart,Ranks.Five)
        ))
        val game = BurracoGameInitTurnTestFactory.create()
                .buildTurnPhaseExecution()

        assertFailsWith(IllegalStateException::class) {
            game.dropOnTableAScale(PlayerIdentity.create(), scale)
        }
    }

    @Test
fun `Given a player during his turn with a tris, when append a card on the tris, the operation is executed`() {
        val tris = BurracoTris(BurracoIdentity.create(), Ranks.Ace, listOf(Card(Suits.Heart, Ranks.Ace), Card(Suits.Heart, Ranks.Ace), Card(Suits.Clover, Ranks.Ace)))
        val player1Id = PlayerIdentity.create()
        val cardToAppend = listOf(Card(Suits.Tile, Ranks.Ace))
        val player1 = PlayerInGame(player1Id, cardToAppend, BurracoCardsOnTable(listOfTris = listOf(tris),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer1(player1)
                .buildTurnPhaseExecution()


        val exceptedTrisSize = tris.numCards() + cardToAppend.size

        val gameActual = game.appendCardsOnABurracoDropped(player1Id, cardToAppend,tris.identity())

        assert(gameActual.playerTrisOnTable(player1Id).size == 1)
        assert(gameActual.playerTrisOnTable(player1Id).first().numCards() == exceptedTrisSize)
        assert(gameActual.playerCards(player1Id).isEmpty())

    }

    @Test
fun `Given a player with a tris and during the turn of another player, when append a card on the tris, I receive an error`() {
        val tris = BurracoTris(BurracoIdentity.create(), Ranks.Ace, listOf(Card(Suits.Heart, Ranks.Ace), Card(Suits.Heart, Ranks.Ace), Card(Suits.Clover, Ranks.Ace)))
        val player1Id = PlayerIdentity.create()
        val cardToAppend = listOf(Card(Suits.Tile, Ranks.Ace))
        val player1 = PlayerInGame(player1Id, cardToAppend, BurracoCardsOnTable(listOfTris = listOf(tris),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .setPlayer2Turn()
                .buildTurnPhaseExecution()

        assertFailsWith(UnsupportedOperationException::class) {
            game.appendCardsOnABurracoDropped(player1Id, cardToAppend,tris.identity())
        }

    }

    @Test
fun `Given a player of another game, with a tris, when append a card on the tris, I receive an error`() {
        val tris = BurracoTris(BurracoIdentity.create(), Ranks.Ace, listOf(Card(Suits.Heart, Ranks.Ace), Card(Suits.Heart, Ranks.Ace), Card(Suits.Clover, Ranks.Ace)))
        val player1Id = PlayerIdentity.create()
        val cardToAppend = listOf(Card(Suits.Tile, Ranks.Ace))
        val player1 = PlayerInGame(player1Id, cardToAppend, BurracoCardsOnTable(listOfTris = listOf(tris),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .buildTurnPhaseExecution()

        assertFailsWith(IllegalStateException::class) {
            game.appendCardsOnABurracoDropped(PlayerIdentity.create(), cardToAppend,tris.identity())
        }
    }


    @Test
fun `Given a player during his turn with no cards, when pickUp the mazzetto, then I can see the mazzetto cards on my hand`() {
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, listOf(), BurracoCardsOnTable(listOfTris = listOf(),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer1(player1)
                .buildTurnPhaseExecution()

        val actualGame = game.pickupMazzetto(player1Id)
        assert(actualGame.playerCards(player1Id).size == 11)

    }

    @Test
fun `Given a player during his turn with some cards, when pickUp the mazzetto, then I receive an error`() {
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, listOf(Card(Suits.Heart, Ranks.Ace)), BurracoCardsOnTable(listOfTris = listOf(),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .buildTurnPhaseExecution()

        assertFailsWith(IllegalStateException::class){
            game.pickupMazzetto(player1Id)
        }
    }

    @Test
fun `Given a player during his turn with no cards and a mazzetto already taken, when pickUp the mazzetto, then I receive an error`() {
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, listOf(Card(Suits.Heart, Ranks.Ace)), BurracoCardsOnTable(listOfTris = listOf(),listOfScale =  listOf()), true)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .buildTurnPhaseExecution()

        assertFailsWith(IllegalStateException::class){
            game.pickupMazzetto(player1Id)
        }
    }

    @Test
fun `Given a player with no cards, during the tun of another player , when pickUp the mazzetto, then I receive an error`() {
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, listOf(), BurracoCardsOnTable(listOfTris = listOf(),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .setPlayer2Turn()
                .buildTurnPhaseExecution()

        assertFailsWith(AssertionError::class){
            game.pickupMazzetto(player1Id)
        }
    }

    @Test
fun `Given a player of another game , when pickUp the mazzetto, then I receive an error`() {
        val player1Id = PlayerIdentity.create()
        val player1 = PlayerInGame(player1Id, listOf(), BurracoCardsOnTable(listOfTris = listOf(),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .setPlayer2Turn()
                .buildTurnPhaseExecution()

        assertFailsWith(NoSuchElementException::class){
            game.pickupMazzetto(PlayerIdentity.create())
        }
    }

    @Test
fun `Given a player during his turn with some cards, when drop a card on a discard pile, then I have a card less`() {
        val player1Id = PlayerIdentity.create()
        val cardToDrop = Card(Suits.Heart,Ranks.Six)
        val player1 = PlayerInGame(player1Id, listOf(cardToDrop), BurracoCardsOnTable(listOfTris = listOf(),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create(player1Id = player1Id)
                .setPlayer1(player1)
                .buildTurnPhaseExecution()

        val expectedDiscardPileSize = 1

        val actualGame = game.dropCardOnDiscardPile(player1Id,cardToDrop)
        assert(actualGame.playerCards(player1Id).isEmpty())
        assert(actualGame.showDiscardPile().size == expectedDiscardPileSize)
        assert(actualGame.showDiscardPile().contains(cardToDrop))

    }

    @Test
fun `Given a player during the turn of another player, when drop a card on a discard pile, then receive an error`() {
        val player1Id = PlayerIdentity.create()
        val cardToDrop = Card(Suits.Heart,Ranks.Six)
        val player1 = PlayerInGame(player1Id, listOf(cardToDrop), BurracoCardsOnTable(listOfTris = listOf(),listOfScale =  listOf()), false)
        val game = BurracoGameInitTurnTestFactory.create()
                .setPlayer1(player1)
                .setPlayer2Turn()
                .buildTurnPhaseExecution()

        assertFailsWith(UnsupportedOperationException::class){
            game.dropCardOnDiscardPile(player1Id,cardToDrop)
        }

    }
}