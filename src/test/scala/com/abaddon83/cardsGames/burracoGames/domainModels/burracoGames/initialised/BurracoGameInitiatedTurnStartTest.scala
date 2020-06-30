package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.mocks.BurracoGameInitTurnTestFactory
import com.abaddon83.cardsGames.shares.decks.{Card, Deck, Ranks, Suits}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameInitiatedTurnStartTest extends AnyFunSuite{


  test("Given the right information, when I build a new Burraco game in the phase turn started, then I create it coreectly"){
    val player1Id = PlayerIdentity()
    val gameIdentity = GameIdentity()
    val game = BurracoGameInitTurnTestFactory(gameIdentity = gameIdentity, player1Id = player1Id)
      .buildTurnPhaseStart()

    assert(game.identity() == gameIdentity)
  }

  test("Given a player in game, when update the cards order, the operation is executed"){
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .buildTurnPhaseStart()

    val playerCards = game.playerCards(player1Id)
    val orderedCards = playerCards.sorted

    val actualGame = game.updatePlayerCardsOrder(player1Id, orderedCards)

    assert(actualGame.playerCards(player1Id) == orderedCards)
    assert(actualGame.playerCards(player1Id) != playerCards)
  }

  test("Given a player not in game, when update the cards order, I receive an error"){
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .buildTurnPhaseStart()

    val playerCards = game.playerCards(player1Id)
    val orderedCards = playerCards.sorted

    assertThrows[NoSuchElementException]{
      game.updatePlayerCardsOrder(PlayerIdentity(), orderedCards)
    }
  }

  test("Given a player during his turn, when pickUps a card from the deck, then I have a card more"){
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .buildTurnPhaseStart()

    val expectedPlayerCards = game.playerCards(player1Id).size+1

    val gameActual = game.pickUpACardFromDeck(player1Id)
    val playerCardsActual = gameActual.playerCards(player1Id)

    assert(expectedPlayerCards == playerCardsActual.size)
  }

  test("Given a player not during his turn, when pickUps a card from the deck, then I receive an error"){
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer2Turn()
      .buildTurnPhaseStart()

    assertThrows[UnsupportedOperationException]{
      game.pickUpACardFromDeck(player1Id)
    }
  }

  test("Given a player during his turn, when pickUps a card from the discard pile , then I have more cards"){
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setDiscardPile(List(Card(Suits.Pike,Ranks.Eight)))
      .buildTurnPhaseStart()

    val discardPileSize =  game.showDiscardPile().size
    val gameActual = game.pickUpCardsFromDiscardPile(player1Id)

    val expectedPlayerCards = game.playerCards(player1Id).size + discardPileSize

    assert(gameActual.playerCards(player1Id).size == expectedPlayerCards)
    assert(gameActual.showDiscardPile().isEmpty)
  }

  test("Given a player during his turn, when pickUps a card from the discard pile , then I receive an error"){
    val player1Id = PlayerIdentity()
    val game = BurracoGameInitTurnTestFactory(player1Id = player1Id)
      .setPlayer2Turn()
      .buildTurnPhaseStart()

    assertThrows[UnsupportedOperationException]{
      game.pickUpCardsFromDiscardPile(player1Id)
    }
  }
}
