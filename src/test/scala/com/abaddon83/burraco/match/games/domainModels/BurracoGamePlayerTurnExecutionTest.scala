package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.`match`.games.domainModels.BurracoGame.BurracoGamePlayerTurnExecution
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.decks.{Card, Suits}
import com.abaddon83.burraco.shares.decks.Ranks.{Ace, Eight, Nine, Seven}
import com.abaddon83.burraco.shares.decks.Suits.Heart
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGamePlayerTurnExecutionTest extends AnyFunSuite{

  private val playerIdentityUUID1 = "061b71f7-a308-4015-9bf2-42bac1c4f6a0"
  private val playerIdentityUUID2 = "ca65f040-eaea-4b9a-8082-c110f4640a15"


  test("player drop a tris using 0 cards, should fail"){
    val tris = Tris(Ace,List.empty)
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(PlayerIdentity(playerIdentityUUID1),tris)
    assertThrows[AssertionError]{
      game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),Tris(List.empty))
    }
  }

  test("player drop a tris using 3 cards"){
    val tris = Tris(Ace,List(Card(Heart,Ace),Card(Heart,Ace),Card(Suits.Clover,Ace)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(PlayerIdentity(playerIdentityUUID1),tris)
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

    val gameActual = game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),tris)

    assert(playerCards.size-tris.showCards.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }

  test("player drop a scale using 0 cards, should fail"){
    val scale = Scale(Heart,List.empty)
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithScale(PlayerIdentity(playerIdentityUUID1),scale)
    assertThrows[AssertionError]{
      game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),Tris(List.empty))
    }
  }

  test("player drop a scale using 3 cards"){
    val scale = Scale(Heart, List(Card(Heart,Seven),Card(Heart,Eight),Card(Heart,Nine)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithScale(PlayerIdentity(playerIdentityUUID1),scale)
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

    val gameActual = game.dropOnTableAScale(PlayerIdentity(playerIdentityUUID1),scale)

    assert(playerCards.size-scale.showCards.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }


  
  private def createBurracoGamePlayerTurnExecution(): BurracoGamePlayerTurnExecution = {
    val game = BurracoGame.BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID1)))
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID2)))
    val gameStart=game.initiate(BurracoDealerFactory(game).dealBurracoCards())
    gameStart.pickUpACardFromDeck(PlayerIdentity(playerIdentityUUID1))
  }

  private def createBurracoGamePlayerTurnExecutionWithPlayerWithTris(playerIdentity: PlayerIdentity, tris: Tris): BurracoGamePlayerTurnExecution = {
    val game = createBurracoGamePlayerTurnExecution()
    game.copy(players = addTrisInThePlayerCards(game,playerIdentity,tris))
  }

  private def createBurracoGamePlayerTurnExecutionWithPlayerWithScale(playerIdentity: PlayerIdentity, scale: Scale): BurracoGamePlayerTurnExecution = {
    val game = createBurracoGamePlayerTurnExecution()
    game.copy(players = addScaleInThePlayerCards(game,playerIdentity,scale))
  }

  private def addScaleInThePlayerCards(game: BurracoGamePlayerTurnExecution, playerIdentity: PlayerIdentity, scale: Scale): List[BurracoPlayerInGame] ={
    game.listOfPlayers().map(bp =>
      if(bp.playerIdentity == playerIdentity){
        val cardsWithScale = List(game.playerCards(bp.playerIdentity).drop(scale.showCards.size), scale.showCards).flatten
        assert(cardsWithScale.containsSlice(scale.showCards))
        BurracoPlayerInGame(bp.playerIdentity,cardsWithScale)
      }else {
        BurracoPlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )
  }

  private def addTrisInThePlayerCards(game: BurracoGamePlayerTurnExecution, playerIdentity: PlayerIdentity, tris: Tris): List[BurracoPlayerInGame] ={
    game.listOfPlayers().map(bp =>
      if(bp.playerIdentity == playerIdentity){
        val cardsWithTris = List(game.playerCards(bp.playerIdentity).drop(tris.showCards.size), tris.showCards).flatten
        assert(cardsWithTris.containsSlice(tris.showCards))
        BurracoPlayerInGame(bp.playerIdentity,cardsWithTris)
      }else {
        BurracoPlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )
  }

}
