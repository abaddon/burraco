package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.shares.games.GameIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoGameTest extends AnyFunSuite{

  test("Given anew game identity, when I create a new Burraco game, then I have a new game") {
    val gameId=GameIdentity()
    val game = BurracoGame.createNewBurracoGame(gameId)
    assert(game.isInstanceOf[BurracoGameWaitingPlayers])
    assert(game.numPlayers == 0)
    assert(game.identity() == gameId)
  }

}
