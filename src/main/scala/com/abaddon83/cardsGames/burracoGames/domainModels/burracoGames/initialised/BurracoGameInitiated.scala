package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos.MazzettoDecks
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

trait BurracoGameInitiated extends BurracoGame {
  override protected val players: List[PlayerInGame]
  protected val playerTurn: PlayerIdentity
  protected val burracoDeck: BurracoDeck
  protected val mazzettoDecks: MazzettoDecks
  protected val discardPile: DiscardPile

  //READ Methods
  def playerCards(playerIdentity: PlayerIdentity): List[Card] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(player) => player.showMyCards()
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def playerTrisOnTable(playerIdentity: PlayerIdentity): List[BurracoTris] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(player) => player.showTrisOnTable()
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def playerScalesOnTable(playerIdentity: PlayerIdentity): List[BurracoScale] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(player) => player.showScalesOnTable()
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

    playersCardsTot + burracoDeck.numCards() + mazzettoDecks.numCards() + discardPile.numCards()
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
