package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class OrganisePlayerCardsCmd(
                                gameIdentity: GameIdentity,
                                playerIdentity: PlayerIdentity,
                                orderedCards: List[Card]
                              ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

class OrganisePlayerCardsHandler(
                                  gameRepositoryPort: GameRepositoryPort
                                )(implicit val ec: scala.concurrent.ExecutionContext)
  extends CommandHandler[OrganisePlayerCardsCmd] {

  override def handleAsync(command: OrganisePlayerCardsCmd): Future[Unit] = {
    for {
      gameInitialised <- gameRepositoryPort.findBurracoGameInitialisedBy(gameIdentity = command.gameIdentity)
      gameInitialisedUpdated = reorderPlayerCards(gameInitialised, command.playerIdentity, command.orderedCards)
    } yield gameRepositoryPort.save(gameInitialisedUpdated)
  }

  override def handle(command: OrganisePlayerCardsCmd): Unit = {
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
