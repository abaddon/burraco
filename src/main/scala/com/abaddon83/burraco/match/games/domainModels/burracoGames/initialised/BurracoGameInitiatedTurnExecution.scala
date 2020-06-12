package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised

import com.abaddon83.burraco.`match`.games.domainModels._
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoGameInitiated
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos.Pozzettos
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGameInitiatedTurnExecution private(
                                                      override val gameIdentity: GameIdentity,
                                                      override protected val players: List[PlayerInGame],
                                                      override protected val burracoDeck: BurracoDeck,
                                                      override protected val pozzettos: Pozzettos,
                                                      override protected val discardPile: DiscardPile,
                                                      override protected val playerTurn: PlayerIdentity
                        ) extends BurracoGameInitiated{

  def updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List[Card]): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    this.copy(
      players = UpdatePlayers(player.copy(cards = orderedCards))
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

  def appendCardsOnAScaleDropped(playerIdentity: PlayerIdentity,cardsToAppend: List[Card],burracoId: BurracoId): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    copy(
      players = UpdatePlayers(player.appendACardOnScaleDropped(burracoId,cardsToAppend))
    ).testInvariants()
  }

  def appendCardsOnATrisDropped(playerIdentity: PlayerIdentity,cardsToAppend: List[Card],burracoId: BurracoId): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    copy(
      players = UpdatePlayers(player.appendACardOnTrisDropped(burracoId,cardsToAppend))
    ).testInvariants()
  }

  def pickupPozzetto(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    assert(player.cards.size ==0,"The player cannot pick up a Pozzetto if he still has cards")
    assert(player.pozzettoTaken == false,"The player cannot pick up a Pozzetto he already taken")

    copy(
      players = UpdatePlayers(player.addPozzettoOnMyCard(pozzettos.firstPozzettoAvailable()))
    ).testInvariants()

  }

  def dropCardOnDiscardPile(playerIdentity: PlayerIdentity, card: Card): BurracoGameInitiatedTurnEnd ={
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    BurracoGameInitiatedTurnEnd.build(
      players = UpdatePlayers(player.dropACard(card)),
      playerTurn = playerTurn,
      burracoDeck = burracoDeck,
      pozzettos = pozzettos,
      discardPile = discardPile.addCard(card),
      gameIdentity = gameIdentity
    ).testInvariants()
  }

}

object BurracoGameInitiatedTurnExecution{
  def apply(
             burracoGame: BurracoGameInitiatedTurnStart,
             players: List[PlayerInGame],
             burracoDeck: BurracoDeck,
             pozzettos: Pozzettos,
             discardPile: DiscardPile,
             playerTurn: PlayerIdentity
             ): BurracoGameInitiatedTurnExecution = {

    val burracoGamePlayerTurnExecution =new BurracoGameInitiatedTurnExecution(
      gameIdentity = burracoGame.gameIdentity,
      players = players,
      burracoDeck = burracoDeck,
      pozzettos = pozzettos,
      discardPile = discardPile,
      playerTurn = playerTurn
    ).testInvariants()

    assert(players.size == burracoGamePlayerTurnExecution.numPlayers)

    burracoGamePlayerTurnExecution
  }
}



