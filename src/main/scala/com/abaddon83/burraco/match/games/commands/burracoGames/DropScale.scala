package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.{BurracoScale, BurracoTris}
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class DropScale(
                     gameIdentity: GameIdentity,
                     playerIdentity: PlayerIdentity,
                     scale: BurracoScale
                   ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class DropScaleHandler(
                       gameRepositoryPort: GameRepositoryPort
                     )
                     (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[DropScale] {

  override def handleAsync(command: DropScale): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity)
      gameExecutionUpdated = gameExecution.dropOnTableAScale(playerIdentity = command.playerIdentity, scale = command.scale)
    } yield gameRepositoryPort.save(gameExecutionUpdated)
  }

  override def handle(command: DropScale): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity), 500 millis)
    val gameExecutionUpdated = gameExecution.dropOnTableAScale(playerIdentity = command.playerIdentity, scale = command.scale)
    gameRepositoryPort.save(gameExecutionUpdated)
  }

}