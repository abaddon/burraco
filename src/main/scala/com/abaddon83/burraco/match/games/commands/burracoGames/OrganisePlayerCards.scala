package com.abaddon83.burraco.`match`.games.commands.burracoGames

import java.util.UUID

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.games.GameIdentity
import com.abaddon83.burraco.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class OrganisePlayerCards(
                                gameIdentity: GameIdentity,
                                playerIdentity: PlayerIdentity,
                                orderedCards: List[Card]
                              ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class OrganisePlayerCardsHandler(
                                  gameRepositoryPort: GameRepositoryPort
                                )(implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[OrganisePlayerCards] {

  override def handleAsync(command: OrganisePlayerCards): Future[Unit] = {
    for {
      gameInitialised <- gameRepositoryPort.findBurracoGameInitialisedBy(gameIdentity = command.gameIdentity)
      gameInitialisedUpdated = reorderPlayerCards(gameInitialised, command.playerIdentity, command.orderedCards)
    } yield gameRepositoryPort.save(gameInitialisedUpdated)
  }

  override def handle(command: OrganisePlayerCards): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    val gameInitialised = Await.result(gameRepositoryPort.findBurracoGameInitialisedBy(gameIdentity = command.gameIdentity), 500 millis)
    val gameInitialisedUpdated = reorderPlayerCards(gameInitialised, command.playerIdentity, command.orderedCards)
    gameRepositoryPort.save(gameInitialisedUpdated)
  }

  private def reorderPlayerCards(game: BurracoGameInitiated, playerIdentity: PlayerIdentity, orderedCards: List[Card]) = {
    game match {
      case game: BurracoGameInitiatedTurnStart => game.updatePlayerCardsOrder(playerIdentity, orderedCards)
      case game: BurracoGameInitiatedTurnExecution => game.updatePlayerCardsOrder(playerIdentity, orderedCards)
      case game: BurracoGameInitiatedTurnEnd => throw new Exception("Not yet implemented the possibility to update card order in the turn end phase ") //game.updatePlayerCardsOrder(playerIdentity,orderedCards)
      case _ => throw new Exception
    }
  }

}
