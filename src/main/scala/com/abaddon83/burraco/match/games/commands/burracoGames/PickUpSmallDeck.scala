package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class PickUpSmallDeck(
                            gameIdentity: GameIdentity,
                            playerIdentity: PlayerIdentity
                          ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class PickUpSmallDeckHandler(
                              gameRepositoryPort: GameRepositoryPort
                            )
                            (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[PickUpSmallDeck] {

  override def handleAsync(command: PickUpSmallDeck): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedBy(gameIdentity = command.gameIdentity)
      updatedGameExecution = pickupSmallDeck(gameExecution, playerIdentity = command.playerIdentity)
    } yield gameRepositoryPort.save(updatedGameExecution)
  }

  override def handle(command: PickUpSmallDeck): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedBy(gameIdentity = command.gameIdentity), 500 millis)
    val updatedGameExecution = pickupSmallDeck(gameExecution, playerIdentity = command.playerIdentity)
    gameRepositoryPort.save(updatedGameExecution)
  }

  private def pickupSmallDeck(game: BurracoGameInitiated, playerIdentity: PlayerIdentity) = {
    game match {
      case game: BurracoGameInitiatedTurnStart => ??? //game.pickupPozzetto(playerIdentity = playerIdentity)
      case game: BurracoGameInitiatedTurnExecution => game.pickupPozzetto(playerIdentity = playerIdentity)
      case game: BurracoGameInitiatedTurnEnd => game.pickupPozzetto(playerIdentity = playerIdentity)
      case _ => throw new Exception
    }
  }

}
