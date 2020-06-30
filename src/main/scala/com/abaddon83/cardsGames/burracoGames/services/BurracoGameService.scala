package com.abaddon83.cardsGames.burracoGames.services

import com.abaddon83.cardsGames.burracoGames.commands.{AddPlayerCmd, CreateNewBurracoGameCmd}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.{GameRepositoryPort, PlayerPort}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.CommandDispatcher

import scala.concurrent.Future


class BurracoGameService(
                          burracoGameRepositoryPort: GameRepositoryPort,
                          playerPort: PlayerPort,
                          commandDispatcher: CommandDispatcher
                  )(implicit val ec: scala.concurrent.ExecutionContext/* = scala.concurrent.ExecutionContext.global*/){

  def createNewBurracoGame(): Future[BurracoGameWaitingPlayers] = {
    val gameIdentity = GameIdentity()
    val command = CreateNewBurracoGameCmd(gameIdentity)
    commandDispatcher.dispatch[CreateNewBurracoGameCmd](command)

    burracoGameRepositoryPort.findBurracoGameWaitingPlayersBy(gameIdentity)
  }

  def addPlayer(gameIdentity: GameIdentity, playerIdentity: PlayerIdentity): Future[BurracoGameWaitingPlayers] = {
    val command = AddPlayerCmd
    for{
      burracoGameWaitingPlayers <- burracoGameRepositoryPort.findBurracoGameWaitingPlayersBy(gameIdentity)
      player <- playerPort.findPlayerNotAssignedBy(playerIdentity)
    } yield burracoGameRepositoryPort.save(burracoGameWaitingPlayers.addPlayer(player))
  }

  def initialiseGame(gameIdentity: GameIdentity /*,playerIdentity: PlayerIdentity*/): Future[BurracoGameInitiatedTurnStart] = {
    for {
      burracoGameWaitingPlayers <- burracoGameRepositoryPort.findBurracoGameWaitingPlayersBy(gameIdentity)
      burracoCardsDealt = BurracoDealerFactory(burracoGameWaitingPlayers).dealBurracoCards()
      burracoGameInitialised = burracoGameWaitingPlayers.start()
    } yield burracoGameRepositoryPort.save(burracoGameInitialised)
  }





}
