package com.abaddon83.cardsGames.burracoGames.commands.burracoGames

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.ports.{GameRepositoryPort, PlayerPort}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class AddPlayer(
                        gameIdentity: GameIdentity,
                        playerIdentity: PlayerIdentity
                        ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


class AddPlayerHandler(
                                 gameRepositoryPort: GameRepositoryPort,
                                 playerPort: PlayerPort
                               )
                               (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[AddPlayer]{

   override def handleAsync(command: AddPlayer): Future[Unit] = {
     assert(gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} doesn't exist")

     for{
       burracoGameWaitingPlayers <- gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity)
       player <- playerPort.findPlayerNotAssignedBy(command.playerIdentity)
     } yield gameRepositoryPort.save(burracoGameWaitingPlayers.addPlayer(player))
  }

  override def handle(command: AddPlayer): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps

    assert(gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} doesn't exist")

    val game = Await.result(gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity), 5000 millis)
    val player = Await.result( playerPort.findPlayerNotAssignedBy(command.playerIdentity), 5000 millis)
    gameRepositoryPort.save(game.addPlayer(player))
  }

}