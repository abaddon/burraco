package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.ended.BurracoGameEnded
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos.MazzettoDecks
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class BurracoGameInitiatedTurnEnd protected(
                                                  override protected val players: List[PlayerInGame],
                                                  override protected val playerTurn: PlayerIdentity,
                                                  override protected val burracoDeck: BurracoDeck,
                                                  override protected val mazzettoDecks: MazzettoDecks,
                                                  override protected val discardPile: DiscardPile,
                                                  override protected val gameIdentity: GameIdentity
                                      ) extends BurracoGameInitiated{

  override def updatePlayerCardsOrder(playerIdentity: PlayerIdentity, orderedCards: List[Card]): BurracoGameInitiated = ???

  def pickupMazzetto(playerIdentity: PlayerIdentity): BurracoGameInitiatedTurnEnd = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    assert(player.showMyCards().size ==0,"The player cannot pick up a Pozzetto if he still has cards")
    assert(player.mazzettoTaken == false,"The player cannot pick up a Pozzetto he already taken")

    val mazzetto = mazzettoDecks.firstMazzettoAvailable()
    mazzettoDecks.mazzettoTaken(mazzetto)

    copy(
      players = UpdatePlayers(player.pickUpMazzetto(mazzetto)),
      mazzettoDecks = mazzettoDecks.mazzettoTaken(mazzetto)
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
      mazzettoDecks = mazzettoDecks,
      discardPile = discardPile,
      playerTurn =nextPlayerTurn
    )
  }

  def completeGame(playerIdentity: PlayerIdentity): BurracoGameEnded = {
    val player = validatePlayerId(playerIdentity)
    validatePlayerTurn(playerIdentity)
    assert(player.showMyCards.size == 0,s"The player cannot complete the game with ${player.showMyCards().size} cards on hand")
    assert(player.mazzettoTaken,s"The player cannot complete the game if the small deck is not taken (status: ${player.mazzettoTaken})")
    assert(player.burracoList().size >0, "The player doesn't have a burraco")
    //TODO add the logic to check if the squad taken the pozzetto

    BurracoGameEnded.build(gameIdentity, players, mazzettoDecks, playerTurn)

  }

  override def listOfPlayers: List[PlayerInGame] = {
    this.players
  }


}

object BurracoGameInitiatedTurnEnd{
  def build(players: List[PlayerInGame], playerTurn: PlayerIdentity, burracoDeck: BurracoDeck, mazzettoDecks: MazzettoDecks, discardPile: DiscardPile, gameIdentity: GameIdentity): BurracoGameInitiatedTurnEnd = {
    new BurracoGameInitiatedTurnEnd(players, playerTurn, burracoDeck, mazzettoDecks, discardPile, gameIdentity)
  }
}

