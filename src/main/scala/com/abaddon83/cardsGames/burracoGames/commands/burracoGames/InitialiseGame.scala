package com.abaddon83.cardsGames.burracoGames.commands.burracoGames

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.burracoGames.services.BurracoDealerFactory
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.util.{Failure, Success}
import scala.concurrent.{Await, Future}

case class InitialiseGame(
                        gameIdentity: GameIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


class InitialiseGameHandler(
                                 gameRepositoryPort: GameRepositoryPort,
                               )
                               (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[InitialiseGame]{

   override def handleAsync(command: InitialiseGame): Future[Unit] = {
     for {
       burracoGame <- gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity)
       burracoGameInitialised = burracoGame.start()
     } yield gameRepositoryPort.save(burracoGameInitialised)
  }

  override def handle(command: InitialiseGame): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val game =Await.result(gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity), 500 millis)
    val burracoCardsDealt = BurracoDealerFactory(game).dealBurracoCards()
    gameRepositoryPort.save(game.start())

  }

}