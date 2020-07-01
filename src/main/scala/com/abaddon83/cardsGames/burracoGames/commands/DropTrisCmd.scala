package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.BurracoTris
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class DropTrisCmd(
                     gameIdentity: GameIdentity,
                     playerIdentity: PlayerIdentity,
                     burracoTris: BurracoTris
                   ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class DropTrisHandler(
                       gameRepositoryPort: GameRepositoryPort
                     )
                     (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[DropTrisCmd] {

  override def handleAsync(command: DropTrisCmd): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity)
      gameExecutionUpdated = gameExecution.dropOnTableATris(playerIdentity = command.playerIdentity, tris = command.burracoTris)
    } yield gameRepositoryPort.save(gameExecutionUpdated)
  }

  override def handle(command: DropTrisCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity), 500 millis)
    val gameExecutionUpdated = gameExecution.dropOnTableATris(playerIdentity = command.playerIdentity, tris = command.burracoTris)
    gameRepositoryPort.save(gameExecutionUpdated)
  }

}