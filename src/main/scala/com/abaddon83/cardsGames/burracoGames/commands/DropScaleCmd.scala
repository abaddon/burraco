package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.BurracoScale
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class DropScaleCmd(
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
  extends CommandHandler[DropScaleCmd] {

  override def handleAsync(command: DropScaleCmd): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity)
      gameExecutionUpdated = gameExecution.dropOnTableAScale(playerIdentity = command.playerIdentity, scale = command.scale)
    } yield gameRepositoryPort.save(gameExecutionUpdated)
  }

  override def handle(command: DropScaleCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity), 500 millis)
    val gameExecutionUpdated = gameExecution.dropOnTableAScale(playerIdentity = command.playerIdentity, scale = command.scale)
    gameRepositoryPort.save(gameExecutionUpdated)
  }

}