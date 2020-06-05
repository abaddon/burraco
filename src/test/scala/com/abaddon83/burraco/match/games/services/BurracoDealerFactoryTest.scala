package com.abaddon83.burraco.`match`.games.services

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoGame, PlayerNotAssigned}
import com.abaddon83.burraco.shares.players.PlayerIdentity
import org.scalatest.funsuite.AnyFunSuite

class BurracoDealerFactoryTest extends AnyFunSuite{

  test(" check BurracoCardsDealt for 2 players"){
    val expectedDiscardPileSize = 1
    val expectedFirstPozzettoSize = 11
    val expectedSecondPozzettoSize = 11
    val expectedDeckSize = 63
    val expectedPlayerCards = 11
    val expectedPlayers = 2

    val game = BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val burracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()

    assert(burracoCardsDealt.discardPile.numCards() == expectedDiscardPileSize)
    assert(burracoCardsDealt.firstPozzettoDeck.numCards() == expectedFirstPozzettoSize)
    assert(burracoCardsDealt.secondPozzettoDeck.numCards() == expectedSecondPozzettoSize)
    assert(burracoCardsDealt.burracoDeck.numCards() == expectedDeckSize)
    burracoCardsDealt.playersCards.values.foreach(list =>
      assert(list.size == expectedPlayerCards)
    )
    assert(burracoCardsDealt.playersCards.size == expectedPlayers)

  }

  test(" check BurracoCardsDealt for 3 players"){
    val expectedDiscardPileSize = 1
    val expectedFirstPozzettoSize = 18
    val expectedSecondPozzettoSize = 11
    val expectedDeckSize = 45
    val expectedPlayerCards = 11
    val expectedPlayers = 3

    val game = BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val burracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()

    assert(burracoCardsDealt.discardPile.numCards() == expectedDiscardPileSize)
    assert(burracoCardsDealt.firstPozzettoDeck.numCards() == expectedFirstPozzettoSize)
    assert(burracoCardsDealt.secondPozzettoDeck.numCards() == expectedSecondPozzettoSize)
    assert(burracoCardsDealt.burracoDeck.numCards() == expectedDeckSize)
    burracoCardsDealt.playersCards.values.foreach(list =>
      assert(list.size == expectedPlayerCards)
    )
    assert(burracoCardsDealt.playersCards.size == expectedPlayers)

  }

  test(" check BurracoCardsDealt for 4 players"){
    val expectedDiscardPileSize = 1
    val expectedFirstPozzettoSize = 11
    val expectedSecondPozzettoSize = 11
    val expectedDeckSize = 41
    val expectedPlayerCards = 11
    val expectedPlayers = 4

    val game = BurracoGame.createNewBurracoGame()
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))
      .addPlayer(PlayerNotAssigned(PlayerIdentity()))

    val burracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()

    assert(burracoCardsDealt.discardPile.numCards() == expectedDiscardPileSize)
    assert(burracoCardsDealt.firstPozzettoDeck.numCards() == expectedFirstPozzettoSize)
    assert(burracoCardsDealt.secondPozzettoDeck.numCards() == expectedSecondPozzettoSize)
    assert(burracoCardsDealt.burracoDeck.numCards() == expectedDeckSize)
    burracoCardsDealt.playersCards.values.foreach(list =>
      assert(list.size == expectedPlayerCards)
    )
    assert(burracoCardsDealt.playersCards.size == expectedPlayers)

  }
}
