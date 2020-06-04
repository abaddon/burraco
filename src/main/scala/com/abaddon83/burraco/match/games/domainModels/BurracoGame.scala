package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.{Game, GameIdentity}
import com.abaddon83.burraco.shares.players.PlayerIdentity


trait BurracoGame extends Game {
  val maxPlayers: Int = 4
  val minPlayers: Int = 2
  val players: List[BurracoPlayer]

}

case class BurracoGameWaitingPlayers(
    override val gameIdentity: GameIdentity,
    override val players: List[BurracoPlayer]
  ) extends BurracoGame{

  def addPlayer(player: PlayerNotAssigned): BurracoGameWaitingPlayers ={
      assert(players.size<maxPlayers,s"Maximum number of players reached, (Max: ${maxPlayers})")
      assert(isAlreadyAPlayer(player) == false, s"The player ${player.playerIdentity.toString()} is already a player of game ${this.gameIdentity.toString()}")
      BurracoGameWaitingPlayers(gameIdentity,List(players,List(player)).flatten)
  }

  def isAlreadyAPlayer(player: BurracoPlayer): Boolean = {
    players.exists(p => p == player)
  }

  def initiate(
                burracoDeck: BurracoDeck,
                firstPozzettoDeck: PozzettoDeck,
                secondPozzettoDeck: PozzettoDeck,
                discardPile: DiscardPile,
                playersCards: Map[PlayerIdentity,List[Card]]
              ): BurracoGameInitialised = {

    assert(players.size >1, s"Not enough players to initiate the game, ( Min: ${minPlayers})")
    //assert(players.exists(p => p.playerIdentity == playerRequestedToStart),"Only the participants can initiate the game")
    assert(players.exists(player => playersCards.keys.exists(p => p == player.playerIdentity)),s"One or more players doesn't have their cards")

    val burracoPlayersInGame =players.map(player =>
      BurracoPlayerInGame(
        player.playerIdentity,
        playersCards.get(player.playerIdentity).get
      )
    )

    val burracoGameInitialised = BurracoGameInitialised(gameIdentity,burracoPlayersInGame,burracoDeck,firstPozzettoDeck,secondPozzettoDeck,discardPile)
    burracoGameInitialised.invariantNumCardsInGame()

    burracoGameInitialised
  }

}

case class BurracoGameInitialised(
            override val gameIdentity: GameIdentity,
            override val players: List[BurracoPlayerInGame],
            burracoDeck: BurracoDeck,
            firstPozzettoDeck: PozzettoDeck,
            secondPozzettoDeck: PozzettoDeck,
            discardPile: DiscardPile
                                 ) extends BurracoGame{

  def invariantNumCardsInGame(): Unit = {
    val playersCardsTot = players.map(player => player.cards.size).foldLeft(0)(_ + _)
    val numCardsInGame =  playersCardsTot + burracoDeck.cards.size + firstPozzettoDeck.cards.size + secondPozzettoDeck.cards.size + discardPile.cards.size
    assert(108 == numCardsInGame,s"The cards in game are not 108. Founds ${numCardsInGame}")
  }

}


object BurracoGame {
  def createNewBurracoGame(): BurracoGameWaitingPlayers ={
    BurracoGameWaitingPlayers(GameIdentity(),List.empty)
  }
}
