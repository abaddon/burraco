package com.abaddon83.burraco.`match`.games.commands.games

import java.util.UUID

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.{GameIdentity, GameTypes}
import com.abaddon83.burraco.shares.games.GameTypes.GameType
import com.abaddon83.libs.cqs.Context
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.Future

case class CreateNewGame(
                        gameIdentity: GameIdentity,
                        gameType: GameType
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


class CreateNewGameHandler(
                                 gameRepositoryPort: GameRepositoryPort
                               )
                               (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[CreateNewGame]{

   override def handleAsync(command: CreateNewGame): Future[Unit] = {
    assert(!gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} already exist")
    Future{
      command.gameType match {
        case GameTypes.Burraco => {
          val burracoGameWaitingPlayer = BurracoGame.createNewBurracoGame(command.gameIdentity)
          gameRepositoryPort.save(burracoGameWaitingPlayer)
        }
      }
      ()
    }
  }

  override def handle(command: CreateNewGame): Unit = {
    assert(!gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} already exist")
    command.gameType match {
      case GameTypes.Burraco => {
        val burracoGameWaitingPlayer = BurracoGame.createNewBurracoGame(command.gameIdentity)
        gameRepositoryPort.save(burracoGameWaitingPlayer)
      }
    }
  }

}