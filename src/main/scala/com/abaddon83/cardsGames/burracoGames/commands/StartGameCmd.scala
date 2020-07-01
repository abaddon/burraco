package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.burracoGames.services.BurracoDealerFactory
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class StartGameCmd(
                        gameIdentity: GameIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


class StartGameHandler(
                                 gameRepositoryPort: GameRepositoryPort
                               )
                      (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[StartGameCmd]{

   override def handleAsync(command: StartGameCmd): Future[Unit] = {
     for {
       burracoGame <- gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity)
       burracoGameInitialised = burracoGame.start()
     } yield gameRepositoryPort.save(burracoGameInitialised)
  }

  override def handle(command: StartGameCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val game =Await.result(gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity), 500 millis)
    gameRepositoryPort.save(game.start())

  }

}