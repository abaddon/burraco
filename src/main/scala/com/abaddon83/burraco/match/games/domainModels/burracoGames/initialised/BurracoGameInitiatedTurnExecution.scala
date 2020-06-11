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
    this.copy(players = playerCardsOrdered(playerIdentity,orderedCards)).testInvariants
  }

  def dropOnTableATris(playerIdentity: PlayerIdentity,tris: BurracoTris): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    UpdatePlayers(player.dropATris(tris))
  }

  def dropOnTableAScale(playerIdentity: PlayerIdentity,scale: BurracoScale): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    UpdatePlayers(player.dropAScale(scale))
  }

  def appendCardsOnAScaleDropped(playerIdentity: PlayerIdentity,cardsToAppend: List[Card],scaleId:ScaleId): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    UpdatePlayers(
      player.appendACardOnScaleDropped(scaleId,cardsToAppend)
    )
  }

  def appendCardsOnATrisDropped(playerIdentity: PlayerIdentity,cardsToAppend: List[Card],trisId:TrisId): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    UpdatePlayers(
      player.appendACardOnTrisDropped(trisId,cardsToAppend)
    )
  }

  def pickupPozzetto(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    assert(player.cards.size ==0,"The player cannot pick up a Pozzetto if he still has cards")
    assert(player.pozzettoTaken == false,"The player cannot pick up a Pozzetto he already taken")

    UpdatePlayers(
      player.addPozzettoOnMyCard(
        pozzettos.firstPozzettoAvailable()
      )
    )
  }

  private def UpdatePlayers(burracoPlayerInGame: PlayerInGame): BurracoGameInitiatedTurnExecution ={
    val updatedPlayers = players.map( playerInGame =>
      if(playerInGame.playerIdentity == burracoPlayerInGame.playerIdentity){
        burracoPlayerInGame
      }else
        playerInGame
    )

    this.copy(players = updatedPlayers).testInvariants()
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


