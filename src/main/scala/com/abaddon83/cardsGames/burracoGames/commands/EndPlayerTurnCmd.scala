package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class EndPlayerTurnCmd(
                          gameIdentity: GameIdentity,
                          playerIdentity: PlayerIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class EndPlayerTurnHandler(
                            gameRepositoryPort: BurracoGameRepositoryPort
                          )
                          (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[EndPlayerTurnCmd] {

  override def handleAsync(command: EndPlayerTurnCmd): Future[Unit] = {
    for {
      gameEnd <- gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity)
      updatedGameEnd = gameEnd.nextPlayerTurn(playerIdentity = command.playerIdentity)
    } yield gameRepositoryPort.save(updatedGameEnd)
  }

  override def handle(command: EndPlayerTurnCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameEnd = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(gameIdentity = command.gameIdentity), 500 millis)
    val updatedGameEnd = gameEnd.nextPlayerTurn(playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(updatedGameEnd)
  }

}
