package com.abaddon83.burraco.mocks

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoDeck, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart, DiscardPile}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.mazzettos.{MazzettoDeck, MazzettoDecks}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.burraco.shares.decks.{Card, Deck}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

import scala.collection.mutable.ListBuffer

case class BurracoGameInitTurnTestFactory(
                                           private val gameIdentity: GameIdentity,
                                           private val player1: PlayerInGame,
                                           private val player2: PlayerInGame,
                                           private val mazzettoDecks: MazzettoDecks,
                                           private val discardPile: DiscardPile,
                                           private val playerTurn: PlayerIdentity
                                         ) {

  def buildTurnPhaseStart(): BurracoGameInitiatedTurnStart = {
    println(s"discardPile().size: ${discardPile.numCards()}")
    BurracoGameInitiatedTurnStart.build(
      gameIdentity = gameIdentity,
      players = List(player1, player2),
      burracoDeck = burracoDeckBuild(),
      mazzettoDecks = mazzettoDecks,
      discardPile = discardPile,
      playerTurn = playerTurn
    )
  }

  def buildTurnPhaseExecution(): BurracoGameInitiatedTurnExecution = {
    BurracoGameInitiatedTurnExecution.build(
      gameIdentity = gameIdentity,
      players = List(player1, player2),
      burracoDeck = burracoDeckBuild(),
      mazzettoDecks = mazzettoDecks,
      discardPile = discardPile,
      playerTurn = playerTurn
    ).testInvariants()
  }

  def buildTurnPhaseEnd(): BurracoGameInitiatedTurnEnd = {
    BurracoGameInitiatedTurnEnd.build(
      gameIdentity = gameIdentity,
      players = List(player1, player2),
      burracoDeck = burracoDeckBuild(),
      mazzettoDecks = mazzettoDecks,
      discardPile = discardPile,
      playerTurn = playerTurn
    ).testInvariants()
  }

  def burracoDeckBuild(): BurracoDeck = {
    BurracoDeck(
      ListBuffer.from(
        (Deck.allRanksWithJollyCards() ++ Deck.allRanksWithJollyCards()).drop(
          discardPile.numCards() +
            mazzettoDecks.numCards() +
            player1.totalPlayerCards() +
            player2.totalPlayerCards()
        )
      )
    )
  }

  def setPlayer1(player: PlayerInGame): BurracoGameInitTurnTestFactory = {
    this.copy(
      player1 = player
    )
  }

  def setPlayer2Turn(): BurracoGameInitTurnTestFactory = {
    this.copy(
      playerTurn = player2.playerIdentity
    )
  }

  def setDiscardPile(list: List[Card]): BurracoGameInitTurnTestFactory = {
    this.copy(discardPile = DiscardPile(list))
  }
}

object BurracoGameInitTurnTestFactory {
  def apply(gameIdentity: GameIdentity = GameIdentity(), player1Id: PlayerIdentity = PlayerIdentity(), player2Id: PlayerIdentity = PlayerIdentity()): BurracoGameInitTurnTestFactory = {

    val player1 = PlayerInGame.build(player1Id, Deck.allRanksCards().take(11))
    val player2 = PlayerInGame.build(player2Id, Deck.allRanksCards().take(11))

    val mazzettoDecks = MazzettoDecks.build(List(
      MazzettoDeck.build(Deck.allRanksCards().take(11)),
      MazzettoDeck.build(Deck.allRanksCards().take(11))
    ))
    val discardPile = DiscardPile.apply(List())

    new BurracoGameInitTurnTestFactory(
      gameIdentity = gameIdentity,
      player1 = player1,
      player2 = player2,
      mazzettoDecks = mazzettoDecks,
      discardPile = discardPile,
      playerTurn = player1.playerIdentity
    )
  }
}

