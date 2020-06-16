package com.abaddon83.burraco.`match`.games.services

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.burraco.`match`.games.ports.{BurracoGameRepositoryPort, PlayerPort}
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity

import scala.concurrent.Future


class BurracoGameService(
                    burracoGameRepositoryPort: BurracoGameRepositoryPort,
                    playerPort: PlayerPort,
                  )(implicit val ec: scala.concurrent.ExecutionContext/* = scala.concurrent.ExecutionContext.global*/){

  def allBurracoGamesWaitingPlayers(): Future[List[BurracoGameWaitingPlayers]] = {
    burracoGameRepositoryPort.findAllBurracoGameWaitingPlayers()
  }

  def createNewBurracoGame(playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = {
    for {
      player <- playerPort.findPlayerNotAssignedBy(playerIdentity)
      burracoGameWaitingPlayer = BurracoGame.createNewBurracoGame()
    } yield burracoGameRepositoryPort.save(burracoGameWaitingPlayer)
  }

  def addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = {
    for{
      burracoGameWaitingPlayers <- burracoGameRepositoryPort.findBurracoGameWaitingPlayersBy(gameIdentity)
      player <- playerPort.findPlayerNotAssignedBy(playerIdentity)
    } yield burracoGameRepositoryPort.save(burracoGameWaitingPlayers.addPlayer(player))
  }

  def initialiseGame(gameIdentity: GameIdentity /*,playerIdentity: PlayerIdentity*/): Future[BurracoGameInitiatedTurnStart] = {
    for {
      burracoGameWaitingPlayers <- burracoGameRepositoryPort.findBurracoGameWaitingPlayersBy(gameIdentity)
      burracoCardsDealt = BurracoDealerFactory(burracoGameWaitingPlayers).dealBurracoCards()
      burracoGameInitialised = burracoGameWaitingPlayers.initiate(burracoCardsDealt)
    } yield burracoGameRepositoryPort.save(burracoGameInitialised)
  }

  



}
