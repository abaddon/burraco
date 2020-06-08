package com.abaddon83.burraco.`match`.games.domainModels.BurracoGame

import com.abaddon83.burraco.`match`.games.domainModels._
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGamePlayerTurnStart private(
                                           override val gameIdentity: GameIdentity,
                                           override protected val players: List[BurracoPlayerInGame],
                                           override protected val burracoDeck: BurracoDeck,
                                           override protected val firstPozzettoDeck: PozzettoDeck,
                                           override protected val secondPozzettoDeck: PozzettoDeck,
                                           override protected val discardPile: DiscardPile,
                                           override protected val playerTurn: PlayerIdentity
                                         ) extends BurracoGamePlayer{

  //WRITE Methods

  //all players can order cards when they want to
  def updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List[Card]): BurracoGamePlayerTurnStart = {
    this.copy(players = playerCardsOrdered(playerIdentity,orderedCards)).testInvariants()
  }

  //When the turn start the player can pickUp a card from the Deck
  def pickUpACardFromDeck(playerIdentity: PlayerIdentity): BurracoGamePlayerTurnExecution = {
    validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    val playersUpdated = players.map(playerInGame =>
      if(playerInGame.playerIdentity == playerIdentity){
        //new BurracoPlayerInGame(playerIdentity, )
        playerInGame.copy(cards = burracoDeck.grabFirstCard() :: playerInGame.cards)
      }else {
        playerInGame
      }
    )

    BurracoGamePlayerTurnExecution(
      this,
      playersUpdated,
      this.burracoDeck,
      this.firstPozzettoDeck,
      this.secondPozzettoDeck,
      this.discardPile,
      this.playerTurn
    ).testInvariants()
  }

  //When the turn start the player can pickUp all cards from the DiscardPile if it's not empty
  def pickUpCardsFromDiscardPile(playerIdentity: PlayerIdentity): BurracoGamePlayerTurnExecution = {
    validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    val playersUpdated = players.map(playerInGame =>
      if(playerInGame.playerIdentity == playerIdentity){
        playerInGame.copy(cards = List(discardPile.grabAllCards(), playerInGame.cards).flatten)
        //new BurracoPlayerInGame(playerIdentity, )
      }else {
        playerInGame
      }
    )

    BurracoGamePlayerTurnExecution(
      this,
      playersUpdated,
      this.burracoDeck,
      this.firstPozzettoDeck,
      this.secondPozzettoDeck,
      this.discardPile,
      this.playerTurn
    ).testInvariants()
  }
}

object BurracoGamePlayerTurnStart{
  def apply(burracoGameWaitingPlayers: BurracoGameWaitingPlayers, burracoCardsDealt: BurracoCardsDealt): BurracoGamePlayerTurnStart = {
    assert(burracoGameWaitingPlayers.listOfPlayers.exists(player => burracoCardsDealt.playersCards.keys.exists(p => p == player.playerIdentity)),s"One or more players doesn't have their cards")

    val burracoPlayersInGame =burracoGameWaitingPlayers.listOfPlayers.map(player =>
      BurracoPlayerInGame(
        player.playerIdentity,
        burracoCardsDealt.playersCards.get(player.playerIdentity).get
      )
    )

    val burracoGameInitialised = BurracoGamePlayerTurnStart(
      burracoGameWaitingPlayers.gameIdentity,
      burracoPlayersInGame,
      burracoCardsDealt.burracoDeck,
      burracoCardsDealt.firstPozzettoDeck,
      burracoCardsDealt.secondPozzettoDeck,
      burracoCardsDealt.discardPile,
      burracoPlayersInGame(0).playerIdentity
    )
    burracoGameInitialised.testInvariants()
  }
}