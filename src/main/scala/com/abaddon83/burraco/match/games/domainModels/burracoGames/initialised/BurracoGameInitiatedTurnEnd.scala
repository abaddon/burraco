package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.completed.BurracoGameCompleted
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{Burraco, PlayerInGame}
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

  def completeGame(playerIdentity: PlayerIdentity): BurracoGameCompleted = {
    val player = validatePlayerId(playerIdentity)
    assert(player.cards.size ==0,"The player cannot pick up a Pozzetto if he still has cards")
    assert(player.pozzettoTaken == false,"The player cannot pick up a Pozzetto he already taken")
    assert(player.cardsOnTable.burracoList().size >0, "The player doesn't have a burraco")
    //TODO add the logic to check if the squad taken the pozzetto

    BurracoGameCompleted.build(gameIdentity, players, pozzettos, playerTurn)
  }

  def BurracoList(playerIdentity: PlayerIdentity): List[Burraco] ={
    //TODO we have to include the squad
    val player = validatePlayerId(playerIdentity)
    player.cardsOnTable.burracoList()
  }

}


object BurracoGameInitiatedTurnEnd{
  def build(players: List[PlayerInGame], playerTurn: PlayerIdentity, burracoDeck: BurracoDeck, pozzettos: Pozzettos, discardPile: DiscardPile, gameIdentity: GameIdentity): BurracoGameInitiatedTurnEnd = {
    new BurracoGameInitiatedTurnEnd(players, playerTurn, burracoDeck, pozzettos, discardPile, gameIdentity)
  }
}

