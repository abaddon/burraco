package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos.MazzettoDecks
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnExecution}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.{BurracoCardsDealt, BurracoGameWaitingPlayers}
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGameInitiatedTurnStart private(
                                                  override protected val gameIdentity: GameIdentity,
                                                  override protected val players: List[PlayerInGame],
                                                  override protected val burracoDeck: BurracoDeck,
                                                  override protected val pozzettos: MazzettoDecks,
                                                  override protected val discardPile: DiscardPile,
                                                  override protected val playerTurn: PlayerIdentity
                                         ) extends BurracoGameInitiated{

  //WRITE Methods
  //all players can order cards when they want to
  def updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List[Card]): BurracoGameInitiatedTurnStart = {
    val player = validatePlayerId(playerIdentity)
    this.copy(
      players = UpdatePlayers(player.copy(cards = orderedCards))
    ).testInvariants
  }

  //When the turn start the player can pickUp a card from the Deck
  def pickUpACardFromDeck(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    BurracoGameInitiatedTurnExecution.build(
      this,
      UpdatePlayers(player.copy(cards = burracoDeck.grabFirstCard() :: player.cards)),
      this.burracoDeck,
      this.pozzettos,
      this.discardPile,
      this.playerTurn
    ).testInvariants()
  }

  //When the turn start the player can pickUp all cards from the DiscardPile if it's not empty
  def pickUpCardsFromDiscardPile(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    BurracoGameInitiatedTurnExecution.build(
      this,
      UpdatePlayers(player.copy(cards = discardPile.grabAllCards() ++ player.cards)),
      this.burracoDeck,
      this.pozzettos,
      this.discardPile,
      this.playerTurn
    ).testInvariants()
  }
}

object BurracoGameInitiatedTurnStart{
  def build(burracoGameWaitingPlayers: BurracoGameWaitingPlayers, burracoCardsDealt: BurracoCardsDealt): BurracoGameInitiatedTurnStart = {
    assert(burracoGameWaitingPlayers.listOfPlayers.exists(player => burracoCardsDealt.playersCards.keys.exists(p => p == player.playerIdentity)),s"One or more players doesn't have their cards")

    val burracoPlayersInGame =burracoGameWaitingPlayers.listOfPlayers.map(player =>
      PlayerInGame(
        player.playerIdentity,
        burracoCardsDealt.playersCards.get(player.playerIdentity).get
      )
    )

    BurracoGameInitiatedTurnStart(
      burracoGameWaitingPlayers.gameIdentity,
      burracoPlayersInGame,
      burracoCardsDealt.burracoDeck,
      MazzettoDecks.build(List(burracoCardsDealt.firstPozzettoDeck,burracoCardsDealt.secondPozzettoDeck)),
      burracoCardsDealt.discardPile,
      burracoPlayersInGame(0).playerIdentity
    ).testInvariants()
  }

  def build(
             gameIdentity: GameIdentity,
             players: List[PlayerInGame],
             burracoDeck: BurracoDeck,
             pozzettos: MazzettoDecks,
             discardPile: DiscardPile,
             playerTurn: PlayerIdentity
           ): BurracoGameInitiatedTurnStart ={

    BurracoGameInitiatedTurnStart(
      gameIdentity,
      players,
      burracoDeck,
      pozzettos,
      discardPile,
      playerTurn
    ).testInvariants()
  }
}