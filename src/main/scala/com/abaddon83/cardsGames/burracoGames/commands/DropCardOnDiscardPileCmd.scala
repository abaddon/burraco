package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class DropCardOnDiscardPileCmd(
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
  extends CommandHandler[DropCardOnDiscardPileCmd] {

  override def handleAsync(command: DropCardOnDiscardPileCmd): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity)
      updatedGameExecution = gameExecution.dropCardOnDiscardPile(playerIdentity = command.playerIdentity, card = command.card)
    } yield gameRepositoryPort.save(updatedGameExecution)
  }

  override def handle(command: DropCardOnDiscardPileCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity), 500 millis)
    val updatedGameExecution = gameExecution.dropCardOnDiscardPile(playerIdentity = command.playerIdentity, card = command.card)
    gameRepositoryPort.save(updatedGameExecution)
  }

}
