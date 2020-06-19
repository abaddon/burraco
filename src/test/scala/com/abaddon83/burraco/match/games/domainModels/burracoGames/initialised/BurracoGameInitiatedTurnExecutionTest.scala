package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoGameInitiatedTurnExecution, playerInGames}
import com.abaddon83.burraco.`match`.games.domainModels.{BurracoId, PlayerNotAssigned, burracoGames}
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.decks.Ranks._
import com.abaddon83.burraco.shares.decks.Suits.{Heart, Tile}
import com.abaddon83.burraco.shares.decks.{Card, Ranks, Suits}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameInitiatedTurnExecutionTest extends AnyFunSuite{

  private val playerIdentityUUID1 = "061b71f7-a308-4015-9bf2-42bac1c4f6a0"
  private val playerIdentityUUID2 = "ca65f040-eaea-4b9a-8082-c110f4640a15"


  test("player drop a tris using 0 cards, should fail"){
    val tris = BurracoTris(BurracoId(),Ace,List.empty)
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(PlayerIdentity(playerIdentityUUID1),tris)
    assertThrows[AssertionError]{
      game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),BurracoTris(List.empty))
    }
  }

  test("player drop a tris using 3 cards"){
    val tris = playerInGames.BurracoTris(BurracoId(),Ace,List(Card(Heart,Ace),Card(Heart,Ace),Card(Suits.Clover,Ace)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(PlayerIdentity(playerIdentityUUID1),tris)
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

    val gameActual = game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),tris)

    assert(playerCards.size-tris.showCards.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }

  test("player drop a scale using 0 cards, should fail"){
    assertThrows[AssertionError]{
      val scale = BurracoScale(List.empty)
      val game = createBurracoGamePlayerTurnExecutionWithPlayerWithScale(PlayerIdentity(playerIdentityUUID1),scale)
      game.dropOnTableAScale(PlayerIdentity(playerIdentityUUID1),scale)
    }
  }

  test("player drop a scale using 3 cards"){
    val scale = BurracoScale(List(Card(Heart,Seven),Card(Heart,Eight),Card(Heart,Nine)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithScale(PlayerIdentity(playerIdentityUUID1),scale)
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

    val gameActual = game.dropOnTableAScale(PlayerIdentity(playerIdentityUUID1),scale)

    assert(playerCards.size-scale.showCards.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }

  test("player append a card on a Tris on the table"){

    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecutionWithATrisDropped(playerIdentity)
    val playerCards = game.playerCards(playerIdentity)
    val cardToAppend = List(Card(Tile,Ace))
    val tris = game.playerTrisOnTable(playerIdentity).head

    val gameActual = game.appendCardsOnATrisDropped(playerIdentity,cardToAppend,tris.getBurracoId())

    assert(playerCards.size - cardToAppend.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
    assert(tris.showCards.size + cardToAppend.size == gameActual.playerTrisOnTable(playerIdentity).find(t => t.getBurracoId() == tris.getBurracoId()).get.showCards.size)

    //assert(playerCards.size-scale.showCards.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }

  test("player append a wrong card on a Tris on the table"){

    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecutionWithATrisDropped(playerIdentity)
    val playerCards = game.playerCards(playerIdentity)
    val cardToAppend = List(playerCards.find(c => c.rank != Ace && c.rank != Two && c.rank != Jolly ).get)
    val tris = game.playerTrisOnTable(playerIdentity).head

    assertThrows[IllegalArgumentException]{
      game.appendCardsOnATrisDropped(playerIdentity,cardToAppend,tris.getBurracoId())
    }
  }

  test("player append a card on a Scala on the table"){

    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecutionWithAScalaDropped(playerIdentity)
    val playerCards = game.playerCards(playerIdentity)
    val cardsToAppend = List(Card(Heart,Ten))
    val scala = game.playerScalesOnTable(playerIdentity).head

    val gameActual = game.appendCardsOnAScaleDropped(playerIdentity,cardsToAppend,scala.getBurracoId())

    assert(playerCards.size - cardsToAppend.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
    assert(scala.showCards.size + cardsToAppend.size == gameActual.playerScalesOnTable(playerIdentity).find(s => s.getBurracoId == scala.getBurracoId).get.showCards.size)

  }

  test("player append a wrong card on a Scala on the table, should fail"){

    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecutionWithAScalaDropped(playerIdentity)
    val playerCards = game.playerCards(playerIdentity)
    val cardsToAppend = List(playerCards.find(c => c.suit != Heart && c.rank != Two && c.rank != Jolly ).get)

    println(s"cardsToAppend.head.toString: ${cardsToAppend.head.toString}")
    val scala = game.playerScalesOnTable(playerIdentity).head

    assertThrows[IllegalArgumentException]{ //AssertionError
      game.appendCardsOnAScaleDropped(playerIdentity,cardsToAppend,scala.getBurracoId)
    }

  }

  test("pick up the pozzetto"){
    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val tris1 = BurracoTris(List(Card(Suits.Heart,Ranks.Four), Card(Suits.Heart,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Clover,Ranks.Four), Card(Suits.Clover,Ranks.Four)))
    val tris2 = BurracoTris(List(Card(Suits.Heart,Ranks.Four), Card(Suits.Heart,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Clover,Ranks.Four), Card(Suits.Clover,Ranks.Four)))

    val game = createBurracoInitiatedPlayerTurnExecution(playerIdentity,tris1.showCards ++ tris2.showCards)

    val gameWithTrisDropped = game.dropOnTableATris(playerIdentity,tris1).dropOnTableATris(playerIdentity,tris2)
    assert(gameWithTrisDropped.playerCards(playerIdentity).isEmpty)

    val gameWithPozzetto1 = gameWithTrisDropped.pickupPozzetto(playerIdentity)
    assert(gameWithPozzetto1.playerCards(playerIdentity).size == 11)

  }

  test("pick up the pozzetto 2 times, should fail"){
    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val tris1 = BurracoTris(List(Card(Suits.Heart,Ranks.Four), Card(Suits.Heart,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Clover,Ranks.Four), Card(Suits.Clover,Ranks.Four)))
    val tris2 = BurracoTris(List(Card(Suits.Heart,Ranks.Four), Card(Suits.Heart,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Tile,Ranks.Four), Card(Suits.Clover,Ranks.Four), Card(Suits.Clover,Ranks.Four)))

    val game = createBurracoInitiatedPlayerTurnExecution(playerIdentity,tris1.showCards ++ tris2.showCards)

    val gameWithTrisDropped = game.dropOnTableATris(playerIdentity,tris1).dropOnTableATris(playerIdentity,tris2)
    assert(gameWithTrisDropped.playerCards(playerIdentity).isEmpty)

    val gameWithPozzetto1 = gameWithTrisDropped.pickupPozzetto(playerIdentity)
    assert(gameWithPozzetto1.playerCards(playerIdentity).size == 11)
    assertThrows[AssertionError] {
      gameWithPozzetto1.pickupPozzetto(playerIdentity)
    }
  }

  test("drop a card on the discard pile") {
    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecution()
    val card = game.playerCards(playerIdentity).head
    val cardSize= game.playerCards(playerIdentity).size
    val gameEnd = game.dropCardOnDiscardPile(playerIdentity,card)
    assert(gameEnd.isInstanceOf[BurracoGameInitiatedTurnEnd])
    assert(gameEnd.playerCards(playerIdentity).size < cardSize)

  }

  private def createBurracoGamePlayerTurnExecutionWithATrisDropped(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnExecution ={
    val tris = initialised.playerInGames.BurracoTris(BurracoId(),Ace,List(Card(Heart,Ace),Card(Heart,Ace),Card(Suits.Clover,Ace)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(playerIdentity,tris)

    game.dropOnTableATris(playerIdentity,tris)
  }

  private def createBurracoGamePlayerTurnExecutionWithAScalaDropped(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnExecution ={
    val scale = BurracoScale(List(Card(Heart,Seven),Card(Heart,Eight),Card(Heart,Nine)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithScale(playerIdentity,scale)

    game.dropOnTableAScale(playerIdentity,scale)
  }

  private def createBurracoGamePlayerTurnExecution(): BurracoGameInitiatedTurnExecution = {
    val game = burracoGames.BurracoGame.createNewBurracoGame(GameIdentity())
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID1)))
      .addPlayer(PlayerNotAssigned(PlayerIdentity(playerIdentityUUID2)))
    val gameStart=game.initiate(BurracoDealerFactory(game).dealBurracoCards())
    gameStart.pickUpACardFromDeck(PlayerIdentity(playerIdentityUUID1))
  }

  private def createBurracoGamePlayerTurnExecutionWithPlayerWithTris(playerIdentity: PlayerIdentity, tris: BurracoTris): BurracoGameInitiatedTurnExecution = {
    val game = createBurracoGamePlayerTurnExecution()
    game.copy(players = addTrisInThePlayerCards(game,playerIdentity,tris))
  }

  private def createBurracoGamePlayerTurnExecutionWithPlayerWithScale(playerIdentity: PlayerIdentity, scale: BurracoScale): BurracoGameInitiatedTurnExecution = {
    val game = createBurracoGamePlayerTurnExecution()
    game.copy(players = addScaleInThePlayerCards(game,playerIdentity,scale))
  }

  private def addScaleInThePlayerCards(game: BurracoGameInitiatedTurnExecution, playerIdentity: PlayerIdentity, scale: BurracoScale): List[PlayerInGame] ={
    game.listOfPlayers().map(bp =>
      if(bp.playerIdentity == playerIdentity){
        val cardsWithScale = List(game.playerCards(bp.playerIdentity).drop(scale.showCards.size+1), scale.showCards,List(Card(Heart,Ten))).flatten
        assert(cardsWithScale.containsSlice(scale.showCards))
        PlayerInGame(bp.playerIdentity,cardsWithScale)
      }else {
        PlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )
  }

  private def addTrisInThePlayerCards(game: BurracoGameInitiatedTurnExecution, playerIdentity: PlayerIdentity, tris: BurracoTris): List[PlayerInGame] ={
    game.listOfPlayers().map(bp =>
      if(bp.playerIdentity == playerIdentity){
        val cardsWithTris = List(game.playerCards(bp.playerIdentity).drop(tris.showCards.size+1), tris.showCards,List(Card(Tile,Ace))).flatten
        assert(cardsWithTris.containsSlice(tris.showCards))
        PlayerInGame(bp.playerIdentity,cardsWithTris)
      }else {
        PlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )
  }

  private def createBurracoInitiatedPlayerTurnExecution(playerIdentity:PlayerIdentity, newPlayerCards: List[Card]): BurracoGameInitiatedTurnExecution ={

    val game = createBurracoGamePlayerTurnExecution()
    val updatedPlayers = game.listOfPlayers.map(bp =>
      if(bp.playerIdentity == playerIdentity){
        PlayerInGame(bp.playerIdentity,newPlayerCards)
      }else {
        PlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )
    game.copy(players = updatedPlayers)
  }



}
