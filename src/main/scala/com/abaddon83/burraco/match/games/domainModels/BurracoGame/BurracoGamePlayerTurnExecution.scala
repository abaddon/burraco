package com.abaddon83.burraco.`match`.games.domainModels.BurracoGame

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoDeck, BurracoPlayerInGame, DiscardPile, PozzettoDeck, Tris}
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

  def dropTris(playerIdentity: PlayerIdentity,tris: Tris): BurracoGamePlayerTurnExecution = {
    validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)

    val updatedPlayerCards = playerCards(playerIdentity: PlayerIdentity) diff tris.showCards

    //TODO drop sul tavolo mancante

    copy(players = UpdatePlayers(BurracoPlayerInGame(playerIdentity,updatedPlayerCards)))

  }

  private def UpdatePlayers(burracoPlayerInGame: BurracoPlayerInGame): List[BurracoPlayerInGame] ={
    players.map( p =>
      if(p.playerIdentity ==burracoPlayerInGame.playerIdentity){
        burracoPlayerInGame
      }else
        p
    )
  }

  //validation


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


