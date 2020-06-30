package com.abaddon83.cardsGames.burracoGames.commands.burracoGames

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.burracoGames.services.BurracoDealerFactory
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.util.{Failure, Success}
import scala.concurrent.{Await, Future}

case class PickUpCardsFromDiscardPile(
                                gameIdentity: GameIdentity,
                                playerIdentity: PlayerIdentity
                              ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class PickUpCardsFromDiscardPileHandler(
                                       gameRepositoryPort: GameRepositoryPort
                                     )
                                     (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[PickUpCardsFromDiscardPile]{

  override def handleAsync(command: PickUpCardsFromDiscardPile): Future[Unit] = {
    for {
      burracoGameStarted <- gameRepositoryPort.findBurracoGameInitialisedTurnStartBy(gameIdentity = command.gameIdentity)
      burracoGameExecution = burracoGameStarted.pickUpCardsFromDiscardPile(playerIdentity = command.playerIdentity)
    } yield gameRepositoryPort.save(burracoGameExecution)
  }

  override def handle(command: PickUpCardsFromDiscardPile): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val burracoGameStarted =Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnStartBy(gameIdentity = command.gameIdentity), 500 millis)
    val burracoGameExecution = burracoGameStarted.pickUpCardsFromDiscardPile(playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(burracoGameExecution)
  }

}