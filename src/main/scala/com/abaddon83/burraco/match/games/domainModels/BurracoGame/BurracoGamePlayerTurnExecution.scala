package com.abaddon83.burraco.`match`.games.domainModels.BurracoGame

import com.abaddon83.burraco.`match`.games.domainModels._
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGamePlayerTurnExecution private(
                          override val gameIdentity: GameIdentity,
                          override protected val players: List[BurracoPlayerInGame],
                          override protected val burracoDeck: BurracoDeck,
                          override protected val firstPozzettoDeck: PozzettoDeck,
                          override protected val secondPozzettoDeck: PozzettoDeck,
                          override protected val discardPile: DiscardPile,
                          override protected val playerTurn: PlayerIdentity
                        ) extends BurracoGamePlayer{

  def updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List[Card]): BurracoGamePlayerTurnExecution = {
    this.copy(players = playerCardsOrdered(playerIdentity,orderedCards)).testInvariants
  }

  def dropOnTableATris(playerIdentity: PlayerIdentity,tris: Tris): BurracoGamePlayerTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    val updatedPlayerCards = player.cards diff tris.showCards
    val updatedPlayerCardsOnTable = player.cardsOnTable.updateListOfTris(tris :: player.cardsOnTable.listOfTris)

    UpdatePlayers(player
      .updateCards(updatedPlayerCards)
      .updateCardsOnTable(updatedPlayerCardsOnTable)
    )
  }

  def dropOnTableAScale(playerIdentity: PlayerIdentity,scale: BurracoScale): BurracoGamePlayerTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    val updatedPlayerCards = player.cards diff scale.showCards
    val updatedPlayerCardsOnTable = player.cardsOnTable.updateListOfScale(scale :: player.cardsOnTable.listOfScale)

    UpdatePlayers(player
      .updateCards(updatedPlayerCards)
      .updateCardsOnTable(updatedPlayerCardsOnTable)
    )
  }

  def appendCardsOnAScaleDropped(playerIdentity: PlayerIdentity,cardsToAppend: List[Card],scaleId:ScaleId): BurracoGamePlayerTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    val updatedPlayerCardsOnTable = player.cardsOnTable.updateScale(scaleId,cardsToAppend)
    val updatedPlayerCards = player.cards diff cardsToAppend

    UpdatePlayers(player
      .updateCards(updatedPlayerCards)
      .updateCardsOnTable(updatedPlayerCardsOnTable)
    )
  }

  def appendCardsOnATrisDropped(playerIdentity: PlayerIdentity,cardsToAppend: List[Card],trisId:TrisId): BurracoGamePlayerTurnExecution = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    val updatedPlayerCards = player.cards diff cardsToAppend
    val updatedPlayerCardsOnTable = player.cardsOnTable.updateTris(trisId,cardsToAppend)

    UpdatePlayers(player
      .updateCards(updatedPlayerCards)
      .updateCardsOnTable(updatedPlayerCardsOnTable)
    )
  }

  private def UpdatePlayers(burracoPlayerInGame: BurracoPlayerInGame): BurracoGamePlayerTurnExecution ={
    val updatedPlayers = players.map( playerInGame =>
      if(playerInGame.playerIdentity ==burracoPlayerInGame.playerIdentity){
        burracoPlayerInGame
      }else
        playerInGame
    )

    this.copy(players = updatedPlayers).testInvariants()
  }

}

object BurracoGamePlayerTurnExecution{
  def apply(
             burracoGame: BurracoGamePlayerTurnStart,
             players: List[BurracoPlayerInGame],
             burracoDeck: BurracoDeck,
             firstPozzettoDeck: PozzettoDeck,
             secondPozzettoDeck: PozzettoDeck,
             discardPile: DiscardPile,
             playerTurn: PlayerIdentity
             ): BurracoGamePlayerTurnExecution = {

    val burracoGamePlayerTurnExecution =new BurracoGamePlayerTurnExecution(
      gameIdentity = burracoGame.gameIdentity,
      players = players,
      burracoDeck = burracoDeck,
      firstPozzettoDeck = firstPozzettoDeck,
      secondPozzettoDeck = secondPozzettoDeck,
      discardPile = discardPile,
      playerTurn = playerTurn
    ).testInvariants()

    assert(players.size == burracoGamePlayerTurnExecution.numPlayers)

    burracoGamePlayerTurnExecution
  }
}


