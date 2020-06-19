package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class CompleteGame(
                         gameIdentity: GameIdentity,
                         playerIdentity: PlayerIdentity
                       ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class CompleteGameHandler(
                           gameRepositoryPort: GameRepositoryPort
                         )
                         (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[CompleteGame] {

  override def handleAsync(command: CompleteGame): Future[Unit] = {
    for {
      gameEnd <- gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity)
      updatedGameEnd = gameEnd.completeGame(playerIdentity = command.playerIdentity)
    } yield gameRepositoryPort.save(updatedGameEnd)
  }

  override def handle(command: CompleteGame): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameEnd = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity), 500 millis)
    val updatedGameEnd = gameEnd.completeGame(playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(updatedGameEnd)
  }

}
