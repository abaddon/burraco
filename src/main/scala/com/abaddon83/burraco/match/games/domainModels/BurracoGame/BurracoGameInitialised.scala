package com.abaddon83.burraco.`match`.games.domainModels.BurracoGame

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoCardsDealt, BurracoDeck, BurracoPlayerInGame, DiscardPile, PozzettoDeck}
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGameInitialised private(
                                           override val gameIdentity: GameIdentity,
                                           override protected val players: List[BurracoPlayerInGame],
                                           private val burracoDeck: BurracoDeck,
                                           private val firstPozzettoDeck: PozzettoDeck,
                                           private val secondPozzettoDeck: PozzettoDeck,
                                           private val discardPile: DiscardPile
                                         ) extends BurracoGame{

  def numCardsInGame: Int = {
    val playersCardsTot = players.map(player => player.cards.size).foldLeft(0)(_ + _)
    playersCardsTot + burracoDeck.numCards() + firstPozzettoDeck.numCards() + secondPozzettoDeck.numCards() + discardPile.numCards()
  }

  def invariantNumCardsInGame(): BurracoGameInitialised = {
    assert(totalCardsRequired == numCardsInGame,s"The cards in game are not ${totalCardsRequired}. Founds ${numCardsInGame}")
    this
  }

  def playerCards(playerIdentity: PlayerIdentity): List[Card] = {
    players.find(p => p.playerIdentity == playerIdentity) match {
      case Some(value) => value.cards.toList
      case None => throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

  def updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List[Card]): BurracoGameInitialised = {
    validatePlayerId(playerIdentity)

    val playersUpdated = players.map(playerInGame =>
      if(playerInGame.playerIdentity == playerIdentity){
        new BurracoPlayerInGame(playerIdentity,orderedCards)
      }else {
        playerInGame
      }
    )
    assert(playersUpdated.size == players.size)
    this.copy(players = playersUpdated).invariantNumCardsInGame
  }

  def pickUpACardFromDeck(playerIdentity: PlayerIdentity): BurracoGameInitialised = {
    validatePlayerId(playerIdentity)

    val playersUpdated = players.map(playerInGame =>
      if(playerInGame.playerIdentity == playerIdentity){
        new BurracoPlayerInGame(playerIdentity, burracoDeck.grabFirstCard() :: playerInGame.cards)
      }else {
        playerInGame
      }
    )
    assert(playersUpdated.size == players.size)

    this.copy(players = playersUpdated).invariantNumCardsInGame
  }

  private def validatePlayerId(playerIdentity: PlayerIdentity) = {
    if(!players.exists(p => p.playerIdentity == playerIdentity)){
      throw new NoSuchElementException(s"Player ${playerIdentity} is not a player of this game ${gameIdentity}")
    }
  }

}

object BurracoGameInitialised{
  def apply(burracoGameWaitingPlayers: BurracoGameWaitingPlayers, burracoCardsDealt: BurracoCardsDealt): BurracoGameInitialised = {
    assert(burracoGameWaitingPlayers.listOfPlayers.exists(player => burracoCardsDealt.playersCards.keys.exists(p => p == player.playerIdentity)),s"One or more players doesn't have their cards")

    val burracoPlayersInGame =burracoGameWaitingPlayers.listOfPlayers.map(player =>
      BurracoPlayerInGame(
        player.playerIdentity,
        burracoCardsDealt.playersCards.get(player.playerIdentity).get
      )
    )

    val burracoGameInitialised = BurracoGameInitialised(
      burracoGameWaitingPlayers.gameIdentity,
      burracoPlayersInGame,
      burracoCardsDealt.burracoDeck,
      burracoCardsDealt.firstPozzettoDeck,
      burracoCardsDealt.secondPozzettoDeck,
      burracoCardsDealt.discardPile
    )
    burracoGameInitialised.invariantNumCardsInGame()
    burracoGameInitialised
  }

  //def playerTurn(): PlayerIdentity ={
  //
  //
  //}
}