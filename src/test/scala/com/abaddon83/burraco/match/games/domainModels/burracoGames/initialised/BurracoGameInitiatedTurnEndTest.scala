package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoCardsOnTable, BurracoTris, PlayerInGame}
import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId, PlayerNotAssigned, burracoGames}
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameInitiatedTurnEndTest extends AnyFunSuite{

  private val playerIdentityUUID1 = "061b71f7-a308-4015-9bf2-42bac1c4f6a0"
  private val playerIdentityUUID2 = "ca65f040-eaea-4b9a-8082-c110f4640a15"

  test("try to pickUp the pozzetto with at least a card, should fail") {
    assertThrows[AssertionError]{
      createGame.pickupPozzetto(PlayerIdentity(playerIdentityUUID1))
    }
  }

  test("try to pickUp the pozzetto  with 0 a card") {
      val game = gameWithPlayerWithNoCards(PlayerIdentity(playerIdentityUUID1))
      val newGame = game.pickupPozzetto(PlayerIdentity(playerIdentityUUID1))
    assert(game.playerCards(PlayerIdentity(playerIdentityUUID1)).size == 0)
    assert(newGame.playerCards(PlayerIdentity(playerIdentityUUID1)).size == 11)
  }

  test("next Player Turn") {
    val game = gameWithPlayerWithNoCards(PlayerIdentity(playerIdentityUUID1))
    val newGame = game.nextPlayerTurn()
    assert(game.validatePlayerTurn(PlayerIdentity(playerIdentityUUID1)) == PlayerIdentity(playerIdentityUUID1))
    assert(newGame.validatePlayerTurn(PlayerIdentity(playerIdentityUUID2)) == PlayerIdentity(playerIdentityUUID2))
  }

  test("complete Game and the player has some cards, should fail"){
    assertThrows[AssertionError]{
      createGame.completeGame(PlayerIdentity(playerIdentityUUID1))
    }
  }

  test("complete Game and the player has 0 cards but no burraco"){
    assertThrows[AssertionError]{
      gameWithPlayerWithNoCards(PlayerIdentity(playerIdentityUUID1)).completeGame(PlayerIdentity(playerIdentityUUID1))
    }
  }

  test("complete Game and the player has 0 cards and a burraco"){

    gameWithPlayerWithBurracoNoCards(PlayerIdentity(playerIdentityUUID1)).completeGame(PlayerIdentity(playerIdentityUUID1))

  }

  private def gameWithPlayerWithBurracoNoCards(playerIdentity: PlayerIdentity) = {
    val game = createGame
    val trisCards = List(
      Card(Suits.Clover,Ranks.Five),
      Card(Suits.Clover,Ranks.Five),
      Card(Suits.Heart,Ranks.Five),
      Card(Suits.Heart,Ranks.Five),
      Card(Suits.Tile,Ranks.Five),
      Card(Suits.Tile,Ranks.Five),
      Card(Suits.Pike,Ranks.Five)
    )
    val burracoCardsOnTable = BurracoCardsOnTable(
      List(BurracoTris(
        BurracoId(),
        Ranks.Five,
        trisCards
      )),
      List()
    )
    val playersUpdated = game.listOfPlayers().map(bp =>
      if(bp.playerIdentity == playerIdentity){
        PlayerInGame(bp.playerIdentity,List(),burracoCardsOnTable)
      }else {
        PlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )
    game.copy(
      players = playersUpdated,
      discardPile =DiscardPile.apply(game.showDiscardPile() ++ game.playerCards(playerIdentity))
    )


  }

  private def gameWithPlayerWithNoCards(playerIdentity: PlayerIdentity) ={
    val game = createGame
    val playersUpdated = game.listOfPlayers().map(bp =>
      if(bp.playerIdentity == playerIdentity){
        PlayerInGame(bp.playerIdentity,List())
      }else {
        PlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )

    game.copy(
      players = playersUpdated,
      discardPile =DiscardPile.apply(game.showDiscardPile() ++ game.playerCards(playerIdentity))
    )

  }

  private def createGame :BurracoGameInitiatedTurnEnd ={
    val game = burracoGames.BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID1)))
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID2)))
    val gameExecution = game
      .initiate(BurracoDealerFactory(game).dealBurracoCards())
      .pickUpACardFromDeck(PlayerIdentity(playerIdentityUUID1))
    gameExecution
      .dropCardOnDiscardPile(PlayerIdentity(playerIdentityUUID1),gameExecution.playerCards(PlayerIdentity(playerIdentityUUID1)).head)

  }

}
