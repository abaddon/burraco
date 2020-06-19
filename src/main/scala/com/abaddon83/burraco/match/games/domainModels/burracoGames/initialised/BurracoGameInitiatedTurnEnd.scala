package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.completed.BurracoGameCompleted
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.pozzettos.Pozzettos
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoGameInitiatedTurnEnd protected(
                                        override protected val players: List[PlayerInGame],
                                        override protected val playerTurn: PlayerIdentity,
                                        override protected val burracoDeck: BurracoDeck,
                                        override protected val pozzettos: Pozzettos,
                                        override protected val discardPile: DiscardPile,
                                        override protected val gameIdentity: GameIdentity
                                      ) extends BurracoGameInitiated{

  def pickupPozzetto(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnEnd = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    assert(player.cards.size ==0,"The player cannot pick up a Pozzetto if he still has cards")
    assert(player.pozzettoTaken == false,"The player cannot pick up a Pozzetto he already taken")

    copy(
      players = UpdatePlayers(player.addPozzettoOnMyCard(pozzettos.firstPozzettoAvailable()))
    ).testInvariants()

  }

  def nextPlayerTurn(playerIdentity: PlayerIdentity) : BurracoGameInitiatedTurnStart = {
    validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    val list = players.map(_.playerIdentity)
    val nextPlayerTurn=list((list.indexOf(playerTurn)+1)%list.size)

    BurracoGameInitiatedTurnStart.build(
      gameIdentity = gameIdentity,
      players =players,
      burracoDeck = burracoDeck,
      pozzettos = pozzettos,
      discardPile = discardPile,
      playerTurn =nextPlayerTurn
    )
  }

  def completeGame(playerIdentity: PlayerIdentity): BurracoGameCompleted = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    assert(player.cards.size == 0,s"The player cannot complete the game with ${player.cards.size} cards on hand")
    assert(player.pozzettoTaken,s"The player cannot complete the game if the small deck is taken ${player.pozzettoTaken}")
    assert(player.cardsOnTable.burracoList().size >0, "The player doesn't have a burraco")
    //TODO add the logic to check if the squad taken the pozzetto

    BurracoGameCompleted.build(gameIdentity, players, pozzettos, playerTurn)

  }

  override def listOfPlayers: List[PlayerInGame] = {
    this.players
  }

}

object BurracoGameInitiatedTurnEnd{
  def build(players: List[PlayerInGame], playerTurn: PlayerIdentity, burracoDeck: BurracoDeck, pozzettos: Pozzettos, discardPile: DiscardPile, gameIdentity: GameIdentity): BurracoGameInitiatedTurnEnd = {
    new BurracoGameInitiatedTurnEnd(players, playerTurn, burracoDeck, pozzettos, discardPile, gameIdentity)
  }
}

