package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.Future

case class CreateNewBurracoGameCmd(
                                 gameIdentity: GameIdentity
                               ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


class CreateNewBurracoGameHandler(
                            gameRepositoryPort: GameRepositoryPort
                          )
                          (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[CreateNewBurracoGameCmd] {

  override def handleAsync(command: CreateNewBurracoGameCmd): Future[Unit] = {
    assert(!gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} already exist")
    Future {
      createNewBurracoGame(command.gameIdentity)
    }
  }

  override def handle(command: CreateNewBurracoGameCmd): Unit = {
    assert(!gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} already exist")
    createNewBurracoGame(command.gameIdentity)
  }

  private def createNewBurracoGame(gameIdentity: GameIdentity): BurracoGameWaitingPlayers = {
    val burracoGameWaitingPlayer = BurracoGame.createNewBurracoGame(gameIdentity)
    gameRepositoryPort.save(burracoGameWaitingPlayer)
  }

}