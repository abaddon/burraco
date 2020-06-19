package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class DropCardOnDiscardPile(
                                  gameIdentity: GameIdentity,
                                  playerIdentity: PlayerIdentity,
                                  card: Card
                                ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class DropCardOnDiscardPileHandler(
                                    gameRepositoryPort: GameRepositoryPort
                                  )
                                  (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[DropCardOnDiscardPile] {

  override def handleAsync(command: DropCardOnDiscardPile): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity)
      updatedGameExecution = gameExecution.dropCardOnDiscardPile(playerIdentity = command.playerIdentity, card = command.card)
    } yield gameRepositoryPort.save(updatedGameExecution)
  }

  override def handle(command: DropCardOnDiscardPile): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity), 500 millis)
    val updatedGameExecution = gameExecution.dropCardOnDiscardPile(playerIdentity = command.playerIdentity, card = command.card)
    gameRepositoryPort.save(updatedGameExecution)
  }

}
