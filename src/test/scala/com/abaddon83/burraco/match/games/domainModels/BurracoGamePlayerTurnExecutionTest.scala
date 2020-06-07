package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.`match`.games.domainModels.BurracoGame.BurracoGamePlayerTurnExecution
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGamePlayerTurnExecutionTest extends AnyFunSuite{

  private val playerIdentityUUID1 = "061b71f7-a308-4015-9bf2-42bac1c4f6a0"
  private val playerIdentityUUID2 = "ca65f040-eaea-4b9a-8082-c110f4640a15"

  def createBurracoGamePlayerTurnExecution(): BurracoGamePlayerTurnExecution = {
    val game = BurracoGame.BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID1)))
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID2)))
    val gameStart=game.initiate(BurracoDealerFactory(game).dealBurracoCards())
    gameStart.pickUpACardFromDeck(PlayerIdentity(playerIdentityUUID1))
  }

  test("player drop a tris using 0 cards, should fail"){
    val game = createBurracoGamePlayerTurnExecution()
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))
    assertThrows[AssertionError]{
      game.dropTris(PlayerIdentity(playerIdentityUUID1),Tris(List.empty))
    }
  }

  test("player drop a tris using 3 cards"){
    val game = createBurracoGamePlayerTurnExecution()
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

   // val gameActual = game.dropTris(PlayerIdentity(playerIdentityUUID1),Tris(List.empty))

   // assert(playerCards.size-3 == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }

}
