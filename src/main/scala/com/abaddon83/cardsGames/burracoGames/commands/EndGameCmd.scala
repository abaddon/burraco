package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class EndGameCmd(
                         gameIdentity: GameIdentity,
                         playerIdentity: PlayerIdentity
                       ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class EndGameHandler(
                           gameRepositoryPort: GameRepositoryPort
                         )
                         (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[EndGameCmd] {

  override def handleAsync(command: EndGameCmd): Future[Unit] = {
    for {
      gameEnd <- gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity)
      updatedGameEnd = gameEnd.completeGame(playerIdentity = command.playerIdentity)
    } yield gameRepositoryPort.save(updatedGameEnd)
  }

  override def handle(command: EndGameCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameEnd = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity), 500 millis)
    val updatedGameEnd = gameEnd.completeGame(playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(updatedGameEnd)
  }

}
