package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.domainModels._
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoTris, PlayerInGame}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos.PozzettoDeck
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

trait BurracoGameInitiated extends BurracoGame {
  override protected val players: List[PlayerInGame]
  protected val playerTurn: PlayerIdentity
  protected val burracoDeck: BurracoDeck
  protected val firstPozzettoDeck: PozzettoDeck
  protected val secondPozzettoDeck: PozzettoDeck
  protected val discardPile: DiscardPile

  //READ Methods
  def playerCards(playerIdentity: PlayerIdentity): List[Card] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(value) => value.cards.toList
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def playerTrisOnTable(playerIdentity: PlayerIdentity): List[BurracoTris] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(player) => player.cardsOnTable.listOfTris
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def playerScalesOnTable(playerIdentity: PlayerIdentity): List[Scale] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(player) => player.cardsOnTable.listOfScale
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def showDiscardPile(): List[Card] = {
    discardPile.showCards
  }

  //WRITE Methods
  protected def playerCardsOrdered(playerIdentity: PlayerIdentity, orderedCards: List[Card]): List[PlayerInGame] = {
    validatePlayerId(playerIdentity)

    val playersUpdated = players.map(playerInGame =>
      if(playerInGame.playerIdentity == playerIdentity){
        playerInGame.copy(cards = orderedCards)
        //new BurracoPlayerInGame(playerIdentity,orderedCards)
      }else {
        playerInGame
      }
    )
    assert(playersUpdated.size == players.size)
    playersUpdated
  }

  //validation
  protected def validatePlayerId(playerIdentity: PlayerIdentity): PlayerInGame = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(playerInGame) => playerInGame
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }

  }

  protected  def validatePlayerTurn(playerIdentity: PlayerIdentity) = {
    if(playerTurn != playerIdentity){
      throw new UnsupportedOperationException(s"It's not the turn of the player ${playerIdentity}")
    }
  }

  protected def numCardsInGame: Int = {
    val playersCardsTot = players.map(player =>
      player.cards.size +
        player.cardsOnTable.listOfTris.map( tris => tris.showCards.size).foldLeft(0)(_ + _) +
        player.cardsOnTable.listOfScale.map( scale => scale.showCards.size).foldLeft(0)(_ + _)
    ).foldLeft(0)(_ + _)
    playersCardsTot + burracoDeck.numCards() + firstPozzettoDeck.numCards() + secondPozzettoDeck.numCards() + discardPile.numCards()
  }


  //Invariants
  def testInvariants(): this.type = {
    invariantNumCardsInGame()
  }

  private def invariantNumCardsInGame(): this.type = {
    assert(totalCardsRequired == numCardsInGame,s"The cards in game are not ${totalCardsRequired}. Founds ${numCardsInGame}")
    this
  }

}
