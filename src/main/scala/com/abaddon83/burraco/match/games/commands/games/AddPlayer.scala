package com.abaddon83.burraco.`match`.games.commands.games

import java.util.UUID

import com.abaddon83.burraco.`match`.games.domainModels.PlayerNotAssigned
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.ports.{GameRepositoryPort, PlayerPort}
import com.abaddon83.burraco.shares.games.{GameIdentity, GameTypes}
import com.abaddon83.burraco.shares.games.GameTypes.GameType
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.Context
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

case class AddPlayer(
                        gameIdentity: GameIdentity,
                        playerIdentity: PlayerIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


case class AddPlayerHandler(
                                 gameRepositoryPort: GameRepositoryPort,
                                 playerPort: PlayerPort
                               )
                               (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[AddPlayer]{

   override def handleAsync(command: AddPlayer): Future[Unit] = {
     assert(gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} doesn't exist")

     for{
       burracoGameWaitingPlayers <- gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity)
       player <- playerPort.findPlayerNotAssignedBy(command.playerIdentity)
     } yield gameRepositoryPort.save(burracoGameWaitingPlayers.addPlayer(player))
  }

  override def handle(command: AddPlayer): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps

    assert(gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} doesn't exist")

    Await.result(
      for{
        burracoGameWaitingPlayers <- gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity)
        player <- playerPort.findPlayerNotAssignedBy(command.playerIdentity)
      } yield gameRepositoryPort.save(burracoGameWaitingPlayers.addPlayer(player))
      , 5000 millis)
  }

}