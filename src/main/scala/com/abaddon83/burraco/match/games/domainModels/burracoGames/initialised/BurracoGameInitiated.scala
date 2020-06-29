package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.completed.BurracoPoint
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos.MazzettoDecks
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

trait BurracoGameInitiated extends BurracoGame {
  override protected val players: List[PlayerInGame]
  protected val playerTurn: PlayerIdentity
  protected val burracoDeck: BurracoDeck
  protected val pozzettos: MazzettoDecks
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
      case Some(player) => player.cardsOnTable.burracoList().filter(b => b.isInstanceOf[BurracoTris]).map(b => b.asInstanceOf[BurracoTris])
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def playerScalesOnTable(playerIdentity: PlayerIdentity): List[BurracoScale] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(player) => player.cardsOnTable.burracoList().filter(b => b.isInstanceOf[BurracoScale]).map(b => b.asInstanceOf[BurracoScale])
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def showDiscardPile(): List[Card] = {
    discardPile.showCards
  }

  // write
  protected def UpdatePlayers(burracoPlayerInGame: PlayerInGame): List[PlayerInGame] = {
    players.map(playerInGame =>
      if (playerInGame.playerIdentity == burracoPlayerInGame.playerIdentity) {
        burracoPlayerInGame
      } else
        playerInGame
    )
  }

  //validation
  protected def validatePlayerId(playerIdentity: PlayerIdentity): PlayerInGame = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(playerInGame) => playerInGame
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }

  }

  def validatePlayerTurn(playerIdentity: PlayerIdentity) = {
    if (playerTurn != playerIdentity) {
      throw new UnsupportedOperationException(s"It's not the turn of the player ${playerIdentity}")
    }
    playerIdentity
  }

  protected def numCardsInGame: Int = {
    val playersCardsTot = players.map(player =>
      player.totalPlayerCards()
    ).foldLeft(0)(_ + _)

    playersCardsTot + burracoDeck.numCards() + pozzettos.numCards() + discardPile.numCards()
  }


  //Invariants
  def testInvariants(): this.type = {
    invariantNumCardsInGame()
  }

  private def invariantNumCardsInGame(): this.type = {
    assert(totalCardsRequired == numCardsInGame, s"The cards in game are not ${totalCardsRequired}. Founds ${numCardsInGame}")
    this
  }

}
