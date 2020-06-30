package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoId
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos.MazzettoDecks
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class BurracoGameInitiatedTurnExecution private(
                                                      override protected val gameIdentity: GameIdentity,
                                                      override protected val players: List[PlayerInGame],
                                                      override protected val burracoDeck: BurracoDeck,
                                                      override protected val mazzettoDecks: MazzettoDecks,
                                                      override protected val discardPile: DiscardPile,
                                                      override protected val playerTurn: PlayerIdentity
                        ) extends BurracoGameInitiated{

  def updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List[Card]): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    this.copy(
      players = UpdatePlayers(player.orderPlayerCards(orderedCards))
    ).testInvariants
  }

  def dropOnTableATris(playerIdentity: PlayerIdentity,tris: BurracoTris): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    copy(
      players = UpdatePlayers(player.dropATris(tris))
    ).testInvariants()
  }

  def dropOnTableAScale(playerIdentity: PlayerIdentity,scale: BurracoScale): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    copy(
      players = UpdatePlayers(player.dropAScale(scale))
    ).testInvariants()
  }

  def appendCardsOnABurracoDropped(playerIdentity: PlayerIdentity,cardsToAppend: List[Card],burracoId: BurracoId): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    copy(
      players = UpdatePlayers(player.appendACardOnBurracoDropped(burracoId,cardsToAppend))
    ).testInvariants()
  }

  def pickupMazzetto(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    assert(player.showMyCards.size ==0,"The player cannot pick up a Mazzetto if he still has cards")
    assert(player.mazzettoTaken == false,"The player cannot pick up a Mazzetto he already taken")

    val mazzetto = mazzettoDecks.firstMazzettoAvailable()
    mazzettoDecks.mazzettoTaken(mazzetto)

    copy(
      players = UpdatePlayers(player.pickUpMazzetto(mazzetto)),
      mazzettoDecks = mazzettoDecks.mazzettoTaken(mazzetto)
    ).testInvariants()

  }

  def dropCardOnDiscardPile(playerIdentity: PlayerIdentity, card: Card): BurracoGameInitiatedTurnEnd ={
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    BurracoGameInitiatedTurnEnd.build(
      players = UpdatePlayers(player.dropACard(card)),
      playerTurn = playerTurn,
      burracoDeck = burracoDeck,
      mazzettoDecks = mazzettoDecks,
      discardPile = discardPile.addCard(card),
      gameIdentity = gameIdentity
    ).testInvariants()
  }

}

object BurracoGameInitiatedTurnExecution{
  def build(
             gameIdentity: GameIdentity,
             players: List[PlayerInGame],
             burracoDeck: BurracoDeck,
             mazzettoDecks: MazzettoDecks,
             discardPile: DiscardPile,
             playerTurn: PlayerIdentity
             ): BurracoGameInitiatedTurnExecution = {

    val burracoGamePlayerTurnExecution =new BurracoGameInitiatedTurnExecution(
      gameIdentity = gameIdentity,
      players = players,
      burracoDeck = burracoDeck,
      mazzettoDecks = mazzettoDecks,
      discardPile = discardPile,
      playerTurn = playerTurn
    ).testInvariants()

    burracoGamePlayerTurnExecution
  }
}



