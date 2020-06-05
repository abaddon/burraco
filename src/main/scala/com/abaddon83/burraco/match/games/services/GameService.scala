package com.abaddon83.burraco.`match`.games.services

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoGame, BurracoGameInitialised, BurracoGameWaitingPlayers}
import com.abaddon83.burraco.`match`.games.ports.{BurracoGameRepositoryPort, PlayerPort}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

import scala.concurrent.Future


class GameService (
                    burracoGameRepositoryPort: BurracoGameRepositoryPort,
                    playerPort: PlayerPort,
                  )(implicit val ec: scala.concurrent.ExecutionContext/* = scala.concurrent.ExecutionContext.global*/){

  def createNewBurracoGame(playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = {
    for {
      player <- playerPort.findPlayerNotAssignedBy(playerIdentity)
      burracoGameWaitingPlayer = BurracoGame.createNewBurracoGame().addPlayer(player)
    } yield burracoGameRepositoryPort.save(burracoGameWaitingPlayer)
  }

  def addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = {
    for{
      burracoGameWaitingPlayers <- burracoGameRepositoryPort.findBurracoGameWaitingPlayersBy(gameIdentity)
      player <- playerPort.findPlayerNotAssignedBy(playerIdentity)
      burracoGame = burracoGameWaitingPlayers.addPlayer(player)
    } yield burracoGame
  }

  def initialiseGame(gameIdentity: GameIdentity /*,playerIdentity: PlayerIdentity*/): Future[BurracoGameInitialised] ={
    for{
      burracoGameWaitingPlayers <- burracoGameRepositoryPort.findBurracoGameWaitingPlayersBy(gameIdentity)
      burracoCardsDealt = BurracoDealerFactory(burracoGameWaitingPlayers).dealBurracoCards()
      burracoGameInitialised = burracoGameWaitingPlayers.initiate(burracoCardsDealt)
    } yield burracoGameInitialised
  }


}
