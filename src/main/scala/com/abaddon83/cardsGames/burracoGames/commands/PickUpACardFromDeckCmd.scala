package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class PickUpACardFromDeckCmd(
                        gameIdentity: GameIdentity,
                        playerIdentity: PlayerIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class PickUpACardFromDeckHandler(
                                 gameRepositoryPort: GameRepositoryPort
                               )
                               (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[PickUpACardFromDeckCmd]{

   override def handleAsync(command: PickUpACardFromDeckCmd): Future[Unit] = {
     for {
       burracoGameStarted <- gameRepositoryPort.findBurracoGameInitialisedTurnStartBy(gameIdentity = command.gameIdentity)
       burracoGameExecution = burracoGameStarted.pickUpACardFromDeck(playerIdentity = command.playerIdentity)
     } yield gameRepositoryPort.save(burracoGameExecution)
  }

  override def handle(command: PickUpACardFromDeckCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val burracoGameStarted =Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnStartBy(gameIdentity = command.gameIdentity), 500 millis)
    val burracoGameExecution = burracoGameStarted.pickUpACardFromDeck(playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(burracoGameExecution)
  }

}