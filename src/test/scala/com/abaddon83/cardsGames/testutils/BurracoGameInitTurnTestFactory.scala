package com.abaddon83.cardsGames.testutils

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos.{MazzettoDeck, MazzettoDecks}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised._
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.shares.decks.{Card, Deck}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

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

  def buildWaitingPlayer(singlePlayer: Boolean = false): BurracoGameWaitingPlayers = {
    BurracoGameWaitingPlayers(
      gameIdentity = gameIdentity,
      players = {
        if (singlePlayer) List(player1)
        else List(player1, player2)
      }
    )
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

