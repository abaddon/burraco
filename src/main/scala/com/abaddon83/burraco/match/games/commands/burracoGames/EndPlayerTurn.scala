package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class EndPlayerTurn(
                          gameIdentity: GameIdentity,
                          playerIdentity: PlayerIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class EndPlayerTurnHandler(
                            gameRepositoryPort: GameRepositoryPort
                          )
                          (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[EndPlayerTurn] {

  override def handleAsync(command: EndPlayerTurn): Future[Unit] = {
    for {
      gameEnd <- gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity)
      updatedGameEnd = gameEnd.nextPlayerTurn(playerIdentity = command.playerIdentity)
    } yield gameRepositoryPort.save(updatedGameEnd)
  }

  override def handle(command: EndPlayerTurn): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameEnd = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity), 500 millis)
    val updatedGameEnd = gameEnd.nextPlayerTurn(playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(updatedGameEnd)
  }

}
