package com.abaddon83.burraco.`match`.games.commands.games

import java.util.UUID

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.GameTypes
import com.abaddon83.burraco.shares.games.GameTypes.GameType
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.Future

case class CreateNewGame(
                        gameType: GameType
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


case class CreateNewGameHandler(
                                 gameRepositoryPort: GameRepositoryPort
                               )
                               (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[CreateNewGame]{


   override def handleAsync(command: CreateNewGame): Future[Unit] = {
    command.gameType match {
      case GameTypes.Burraco =>
        for {
          burracoGameWaitingPlayer <- Future{BurracoGame.createNewBurracoGame()}
        } yield gameRepositoryPort.save(burracoGameWaitingPlayer)
      }
  }

  override def handle(command: CreateNewGame): Unit = ???

}