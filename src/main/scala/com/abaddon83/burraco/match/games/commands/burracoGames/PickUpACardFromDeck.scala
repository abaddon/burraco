package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.`match`.games.services.BurracoDealerFactory
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}
import scala.util.{Failure, Success}

import scala.concurrent.{Await, Future}

case class PickUpACardFromDeck(
                        gameIdentity: GameIdentity,
                        playerIdentity: PlayerIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class PickUpACardFromDeckHandler(
                                 gameRepositoryPort: GameRepositoryPort
                               )
                               (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[PickUpACardFromDeck]{

   override def handleAsync(command: PickUpACardFromDeck): Future[Unit] = {
     for {
       burracoGameStarted <- gameRepositoryPort.findBurracoGameInitialisedTurnStartBy(gameIdentity = command.gameIdentity)
       burracoGameExecution = burracoGameStarted.pickUpACardFromDeck(playerIdentity = command.playerIdentity)
     } yield gameRepositoryPort.save(burracoGameExecution)
  }

  override def handle(command: PickUpACardFromDeck): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val burracoGameStarted =Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnStartBy(gameIdentity = command.gameIdentity), 500 millis)
    val burracoGameExecution = burracoGameStarted.pickUpACardFromDeck(playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(burracoGameExecution)
  }

}