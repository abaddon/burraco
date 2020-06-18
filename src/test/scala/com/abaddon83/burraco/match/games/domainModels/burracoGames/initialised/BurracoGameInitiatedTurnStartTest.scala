package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.burraco.`match`.games.domainModels.{PlayerNotAssigned, burracoGames}
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameInitiatedTurnStartTest extends AnyFunSuite{

  private val playerIdentityUUID1 = "061b71f7-a308-4015-9bf2-42bac1c4f6a0"
  private val playerIdentityUUID2 = "ca65f040-eaea-4b9a-8082-c110f4640a15"

  def createBurracoGamePlayerTurnStart(): BurracoGameInitiatedTurnStart = {
    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID1)))
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID2)))

    game.initiate(BurracoDealerFactory(game).dealBurracoCards())
  }

  test("player pickUp a card from the deck"){
    val game = createBurracoGamePlayerTurnStart()
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

    val gameActual = game.pickUpACardFromDeck(PlayerIdentity(playerIdentityUUID1))
    val playerCardsActual = gameActual.playerCards(PlayerIdentity(playerIdentityUUID1))

    assert(playerCards.size+1 == playerCardsActual.size)
  }

  test("player 2 pickUp cards from the deck during the player 1 turn, should fail"){
    val game = createBurracoGamePlayerTurnStart()
    assertThrows[UnsupportedOperationException]{
      game.pickUpACardFromDeck(PlayerIdentity(playerIdentityUUID2))
    }
  }

  test("player pickUp cards from DiscardPile"){
    val game = createBurracoGamePlayerTurnStart()
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))
    val discardPile: List[Card] = game.showDiscardPile()

    val gameActual = game.pickUpCardsFromDiscardPile(PlayerIdentity(playerIdentityUUID1))
    val playerCardsActual = gameActual.playerCards(PlayerIdentity(playerIdentityUUID1))

    assert(playerCards.size+discardPile.size == playerCardsActual.size)
    assert(gameActual.showDiscardPile().isEmpty)
  }

  test("player 2 pickUp cards from DiscardPile during the player 1 turn, should fail"){
    val game = createBurracoGamePlayerTurnStart()
    assertThrows[UnsupportedOperationException]{
      game.pickUpCardsFromDiscardPile(PlayerIdentity(playerIdentityUUID2))
    }
  }
}
