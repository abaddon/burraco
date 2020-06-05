package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.games.{Game, GameIdentity}


trait BurracoGame extends Game {
  val maxPlayers: Int = 4
  val minPlayers: Int = 2
  val totalCardsInGame: Int = 108
  protected val players: List[BurracoPlayer]

  def numPlayers: Int = {
    players.size
  }

  def listOfPlayers(): List[BurracoPlayer]= {
    players
  }


}

case class BurracoGameWaitingPlayers(
    override val gameIdentity: GameIdentity,
    override protected val players: List[BurracoPlayer]
  ) extends BurracoGame{

  def addPlayer(player: PlayerNotAssigned): BurracoGameWaitingPlayers ={
      assert(players.size<maxPlayers,s"Maximum number of players reached, (Max: ${maxPlayers})")
      assert(isAlreadyAPlayer(player) == false, s"The player ${player.playerIdentity.toString()} is already a player of game ${this.gameIdentity.toString()}")
      BurracoGameWaitingPlayers(gameIdentity,List(players,List(player)).flatten)
  }

  def isAlreadyAPlayer(player: BurracoPlayer): Boolean = {
    players.exists(p => p == player)
  }

  def initiate(burracoCardsDealt: BurracoCardsDealt): BurracoGameInitialised = {

    assert(players.size >1, s"Not enough players to initiate the game, ( Min: ${minPlayers})")
    //assert(players.exists(p => p.playerIdentity == playerRequestedToStart),"Only the participants can initiate the game")

    BurracoGameInitialised(this,burracoCardsDealt)
  }

}

object BurracoGame {
  def createNewBurracoGame(): BurracoGameWaitingPlayers ={
    BurracoGameWaitingPlayers(GameIdentity(),List.empty)
  }
}

case class BurracoGameInitialised private(
            override val gameIdentity: GameIdentity,
            override protected val players: List[BurracoPlayerInGame],
            burracoDeck: BurracoDeck,
            firstPozzettoDeck: PozzettoDeck,
            secondPozzettoDeck: PozzettoDeck,
            discardPile: DiscardPile
                                 ) extends BurracoGame{


  def numCardsInGame: Int = {
    val playersCardsTot = players.map(player => player.cards.size).foldLeft(0)(_ + _)
    playersCardsTot + burracoDeck.numCards() + firstPozzettoDeck.numCards() + secondPozzettoDeck.numCards() + discardPile.numCards()
  }

  def invariantNumCardsInGame(): Unit = {
    assert(totalCardsInGame == numCardsInGame,s"The cards in game are not ${totalCardsInGame}. Founds ${numCardsInGame}")
  }

}

object BurracoGameInitialised{
  def apply(burracoGameWaitingPlayers: BurracoGameWaitingPlayers, burracoCardsDealt: BurracoCardsDealt): BurracoGameInitialised = {
    assert(burracoGameWaitingPlayers.listOfPlayers.exists(player => burracoCardsDealt.playersCards.keys.exists(p => p == player.playerIdentity)),s"One or more players doesn't have their cards")

    val burracoPlayersInGame =burracoGameWaitingPlayers.listOfPlayers.map(player =>
      BurracoPlayerInGame(
        player.playerIdentity,
        burracoCardsDealt.playersCards.get(player.playerIdentity).get
      )
    )

    val burracoGameInitialised = BurracoGameInitialised(
      burracoGameWaitingPlayers.gameIdentity,
      burracoPlayersInGame,
      burracoCardsDealt.burracoDeck,
      burracoCardsDealt.firstPozzettoDeck,
      burracoCardsDealt.secondPozzettoDeck,
      burracoCardsDealt.discardPile
    )
    burracoGameInitialised.invariantNumCardsInGame()

    burracoGameInitialised
  }
}


