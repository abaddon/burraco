package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.domainModels.BurracoId
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.playerInGames.BurracoTris
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class AppendCardOnBurraco(
                                gameIdentity: GameIdentity,
                                playerIdentity: PlayerIdentity,
                                burracoId: BurracoId,
                                cardsToAppend: List[Card]
                              ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class AppendCardOnBurracoHandler(
                                  gameRepositoryPort: GameRepositoryPort
                                )
                                (implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[AppendCardOnBurraco] {

  override def handleAsync(command: AppendCardOnBurraco): Future[Unit] = {
    for {
      gameExecution <- gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity)
      gameExecutionUpdated = gameExecution.appendCardsOnABurracoDropped(playerIdentity = command.playerIdentity,cardsToAppend = command.cardsToAppend,burracoId = command.burracoId )
    } yield gameRepositoryPort.save(gameExecutionUpdated)
  }

  override def handle(command: AppendCardOnBurraco): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameExecution = Await.result(gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(gameIdentity = command.gameIdentity), 500 millis)
    val gameExecutionUpdated = gameExecution.appendCardsOnABurracoDropped(playerIdentity = command.playerIdentity,cardsToAppend = command.cardsToAppend,burracoId = command.burracoId )
    gameRepositoryPort.save(gameExecutionUpdated)
  }

}