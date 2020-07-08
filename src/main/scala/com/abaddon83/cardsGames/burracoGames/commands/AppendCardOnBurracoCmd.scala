package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.BurracoId
import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameRepositoryPort
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class AppendCardOnBurracoCmd(
                                gameIdentity: GameIdentity,
                                playerIdentity: PlayerIdentity,
                                burracoId: BurracoId,
                                cardsToAppend: List[Card]
                              ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class AppendCardOnBurracoHandler(
                                  gameRepositoryPort: BurracoGameRepositoryPort
                                )
                                (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[AppendCardOnBurracoCmd] {

  override def handleAsync(command: AppendCardOnBurracoCmd): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity)
      gameExecutionUpdated = gameExecution.appendCardsOnABurracoDropped(playerIdentity = command.playerIdentity,cardsToAppend = command.cardsToAppend,burracoId = command.burracoId )
    } yield gameRepositoryPort.save(gameExecutionUpdated)
  }

  override def handle(command: AppendCardOnBurracoCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity), 500 millis)
    val gameExecutionUpdated = gameExecution.appendCardsOnABurracoDropped(playerIdentity = command.playerIdentity,cardsToAppend = command.cardsToAppend,burracoId = command.burracoId )
    gameRepositoryPort.save(gameExecutionUpdated)
  }

}