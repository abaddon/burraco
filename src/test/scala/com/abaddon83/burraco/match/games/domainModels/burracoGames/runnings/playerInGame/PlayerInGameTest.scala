package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.runnings.playerInGame

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris, PlayerInGame}
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class PlayerInGameTest extends AnyFunSuite{



  test("add the mazzetto cards on my cards") {

  }

}

object PlayerInGameTestFactory {
  def apply(): PlayerInGameTestFactory = {
    apply(PlayerIdentity())
  }
  def apply(playerIdentity: PlayerIdentity): PlayerInGameTestFactory ={
    PlayerInGameTestFactory(
      playerInGame = PlayerInGame(playerIdentity,List[Card]().empty)
    )
  }

}

case class PlayerInGameTestFactory(private val playerInGame : PlayerInGame) {

  def withCards(cards: List[Card]):PlayerInGameTestFactory = {
    this.copy(
      playerInGame = playerInGame.copy(
        cards = cards
      )
    )
  }

  def withTrisOnTable(burracoTris: BurracoTris):PlayerInGameTestFactory = {
    val updatedList =playerInGame.cardsOnTable.listOfTris ++ List(burracoTris)

    this.copy(
      playerInGame = playerInGame.copy(
        cardsOnTable = playerInGame.cardsOnTable.copy(listOfTris = updatedList )
      )
    )
  }

  def withScalaOnTable(burracoScale: BurracoScale):PlayerInGameTestFactory = {
    val updatedList =playerInGame.cardsOnTable.listOfScale ++ List(burracoScale)

    this.copy(
      playerInGame = playerInGame.copy(
        cardsOnTable = playerInGame.cardsOnTable.copy(listOfScale = updatedList )
      )
    )
  }



}
