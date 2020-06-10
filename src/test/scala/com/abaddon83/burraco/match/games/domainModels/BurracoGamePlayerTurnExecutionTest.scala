package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.`match`.games.domainModels.BurracoGame.BurracoGamePlayerTurnExecution
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.decks.Ranks.{Ace, Eight, Jolly, Nine, Seven, Ten, Two}
import com.abaddon83.burraco.shares.decks.Suits.{Heart, Tile}
import com.abaddon83.burraco.shares.decks.{Card, Suits}
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGamePlayerTurnExecutionTest extends AnyFunSuite{

  private val playerIdentityUUID1 = "061b71f7-a308-4015-9bf2-42bac1c4f6a0"
  private val playerIdentityUUID2 = "ca65f040-eaea-4b9a-8082-c110f4640a15"


  test("player drop a tris using 0 cards, should fail"){
    val tris = Tris(TrisId(),Ace,List.empty)
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(PlayerIdentity(playerIdentityUUID1),tris)
    assertThrows[AssertionError]{
      game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),Tris(List.empty))
    }
  }

  test("player drop a tris using 3 cards"){
    val tris = Tris(TrisId(),Ace,List(Card(Heart,Ace),Card(Heart,Ace),Card(Suits.Clover,Ace)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(PlayerIdentity(playerIdentityUUID1),tris)
    val playerCards = game.playerCards(PlayerIdentity(playerIdentityUUID1))

    val gameActual = game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),tris)

    assert(playerCards.size-tris.showCards.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }

  test("player drop a scale using 0 cards, should fail"){
    val scale = Scale(List.empty)
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithScale(PlayerIdentity(playerIdentityUUID1),scale)
    assertThrows[AssertionError]{
      game.dropOnTableATris(PlayerIdentity(playerIdentityUUID1),Tris(List.empty))
    }
  }

  test("player drop a scale using 3 cards"){
    val scale = Scale(List(Card(Heart,Seven),Card(Heart,Eight),Card(Heart,Nine)))
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

    val gameActual = game.appendCardsOnATrisDropped(playerIdentity,cardToAppend,tris.trisId)

    assert(playerCards.size - cardToAppend.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
    assert(tris.showCards.size + cardToAppend.size == gameActual.playerTrisOnTable(playerIdentity).find(t => t.trisId == tris.trisId).get.showCards.size)

    //assert(playerCards.size-scale.showCards.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
  }

  test("player append a wrong card on a Tris on the table"){

    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecutionWithATrisDropped(playerIdentity)
    val playerCards = game.playerCards(playerIdentity)
    val cardToAppend = List(playerCards.find(c => c.rank != Ace && c.rank != Two && c.rank != Jolly ).get)
    val tris = game.playerTrisOnTable(playerIdentity).head

    assertThrows[IllegalArgumentException]{
      game.appendCardsOnATrisDropped(playerIdentity,cardToAppend,tris.trisId)
    }
  }

  test("player append a card on a Scala on the table"){

    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecutionWithAScalaDropped(playerIdentity)
    val playerCards = game.playerCards(playerIdentity)
    val cardsToAppend = List(Card(Heart,Ten))
    val scala = game.playerScalesOnTable(playerIdentity).head

    val gameActual = game.appendCardsOnAScaleDropped(playerIdentity,cardsToAppend,scala.getScaleId)

    assert(playerCards.size - cardsToAppend.size == gameActual.playerCards(PlayerIdentity(playerIdentityUUID1)).size)
    assert(scala.showCards.size + cardsToAppend.size == gameActual.playerScalesOnTable(playerIdentity).find(s => s.getScaleId == scala.getScaleId).get.showCards.size)

  }

  test("player append a wrong card on a Scala on the table, should fail"){

    val playerIdentity = PlayerIdentity(playerIdentityUUID1)
    val game = createBurracoGamePlayerTurnExecutionWithAScalaDropped(playerIdentity)
    val playerCards = game.playerCards(playerIdentity)
    val cardsToAppend = List(playerCards.find(c => c.suit != Heart && c.rank != Two && c.rank != Jolly ).get)

    println(s"cardsToAppend.head.toString: ${cardsToAppend.head.toString}")
    val scala = game.playerScalesOnTable(playerIdentity).head

    assertThrows[IllegalArgumentException]{
      game.appendCardsOnAScaleDropped(playerIdentity,cardsToAppend,scala.getScaleId)
    }

  }



  private def createBurracoGamePlayerTurnExecutionWithATrisDropped(playerIdentity: PlayerIdentity): BurracoGamePlayerTurnExecution ={
    val tris = Tris(TrisId(),Ace,List(Card(Heart,Ace),Card(Heart,Ace),Card(Suits.Clover,Ace)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithTris(playerIdentity,tris)

    game.dropOnTableATris(playerIdentity,tris)
  }

  private def createBurracoGamePlayerTurnExecutionWithAScalaDropped(playerIdentity: PlayerIdentity): BurracoGamePlayerTurnExecution ={
    val scale = Scale(List(Card(Heart,Seven),Card(Heart,Eight),Card(Heart,Nine)))
    val game = createBurracoGamePlayerTurnExecutionWithPlayerWithScale(playerIdentity,scale)

    game.dropOnTableAScale(playerIdentity,scale)
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
        val cardsWithScale = List(game.playerCards(bp.playerIdentity).drop(scale.showCards.size+1), scale.showCards,List(Card(Heart,Ten))).flatten
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
        val cardsWithTris = List(game.playerCards(bp.playerIdentity).drop(tris.showCards.size+1), tris.showCards,List(Card(Tile,Ace))).flatten
        assert(cardsWithTris.containsSlice(tris.showCards))
        BurracoPlayerInGame(bp.playerIdentity,cardsWithTris)
      }else {
        BurracoPlayerInGame(bp.playerIdentity,game.playerCards(bp.playerIdentity))
      }
    )
  }

}
