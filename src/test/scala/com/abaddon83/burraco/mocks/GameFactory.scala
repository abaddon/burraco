package com.abaddon83.burraco.mocks

import com.abaddon83.burraco.`match`.games.commands.burracoGames.{AddPlayer, AddPlayerHandler, InitialiseGame, InitialiseGameHandler}
import com.abaddon83.burraco.`match`.games.commands.games.{CreateNewGame, CreateNewGameHandler}
import com.abaddon83.burraco.`match`.games.ports.{GameRepositoryPort, PlayerPort}
import com.abaddon83.burraco.shares.games.{GameIdentity, GameTypes}
import com.abaddon83.burraco.shares.players.PlayerIdentity


case class GameFactory(
                        gameIdentity: GameIdentity,
                        gameRepositoryPort: GameRepositoryPort,
                        playerPort: PlayerPort)(implicit val ec: scala.concurrent.ExecutionContext) {

  def addPlayer(playerIdentity: PlayerIdentity): GameFactory = {
    AddPlayerHandler(gameRepositoryPort = gameRepositoryPort,playerPort = playerPort ).handle(
      AddPlayer(gameIdentity = gameIdentity, playerIdentity = playerIdentity)
    )
    this
  }

  def initialise(playerIdentity: PlayerIdentity): GameFactory = {
    InitialiseGameHandler(gameRepositoryPort = gameRepositoryPort).handle(
      InitialiseGame(gameIdentity = gameIdentity)
    )
    this
  }

}

object GameFactory{
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  def build(gameIdentity: GameIdentity,
            gameRepositoryPort: GameRepositoryPort,
            playerPort: PlayerPort
           ): GameFactory = {

    CreateNewGameHandler(gameRepositoryPort = gameRepositoryPort).handle(
      CreateNewGame(gameIdentity = gameIdentity,GameTypes.Burraco)
    )
    new GameFactory(gameIdentity, gameRepositoryPort, playerPort)
  }
}
