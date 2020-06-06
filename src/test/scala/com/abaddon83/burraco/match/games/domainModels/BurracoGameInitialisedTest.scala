package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.`match`.games.domainModels.BurracoGame.BurracoGameInitialised
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameInitialisedTest extends AnyFunSuite{

  private val playerIdentityUUID1 = "061b71f7-a308-4015-9bf2-42bac1c4f6a0"
  private val playerIdentityUUID2 = "ca65f040-eaea-4b9a-8082-c110f4640a15"

  def createBurracoGameInitialised(): BurracoGameInitialised = {
    val game = BurracoGame.BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID1)))
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID2)))

    game.initiate(BurracoDealerFactory(game).dealBurracoCards())
  }

  test("show the players cards") {
    val game = createBurracoGameInitialised()
    assert(game.playerCards(PlayerIdentity(playerIdentityUUID1)).size == 11)
    assert(game.playerCards(PlayerIdentity(playerIdentityUUID2)).size == 11)
  }

  test("reorder the player cards") {
    val game = createBurracoGameInitialised()
    val cards1 = game.playerCards(PlayerIdentity(playerIdentityUUID1))
    val expectedCards2 = game.playerCards(PlayerIdentity(playerIdentityUUID2))

    val expectedCardsOrdered1: List[Card] = scala.util.Random.shuffle(cards1)

    val gameUpdated = game.updatePlayerCardsOrder(PlayerIdentity(playerIdentityUUID1),expectedCardsOrdered1)

    val cardsOrdered1 = gameUpdated.playerCards(PlayerIdentity(playerIdentityUUID1))
    val cards2 = gameUpdated.playerCards(PlayerIdentity(playerIdentityUUID2))
    (0 to 10).foreach(i =>
      assert(cardsOrdered1(i) == expectedCardsOrdered1(i) && cards2(i) == expectedCards2(i))
    )
    assert(gameUpdated.gameIdentity == game.gameIdentity)
  }

  test("player pickUp a card from the deck"){
    val game = createBurracoGameInitialised()
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

    val gameActual = game.pickUpACardFromDeck(PlayerIdentity(playerIdentityUUID1))
    val playerCardsActual = gameActual.playerCards(PlayerIdentity(playerIdentityUUID1))

    assert(playerCards.size+1 == playerCardsActual.size)



  }



}
