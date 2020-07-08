package com.abaddon83.cardsGames.burracoGames.commands

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.PlayerNotAssigned
import com.abaddon83.cardsGames.burracoGames.ports.{BurracoGameRepositoryPort, PlayerPort}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}

import scala.concurrent.{Await, Future}

case class AddPlayerCmd(
                         gameIdentity: GameIdentity,
                         playerToAdd: PlayerNotAssigned
                       ) extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}


class AddPlayerHandler(
                        gameRepositoryPort: BurracoGameRepositoryPort
                      )
                      (implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[AddPlayerCmd] {

  override def handleAsync(command: AddPlayerCmd): Future[Unit] = {
    assert(gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} doesn't exist")
    for {
      burracoGameWaitingPlayers <- gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity)
    } yield gameRepositoryPort.save(burracoGameWaitingPlayers.addPlayer(command.playerToAdd))
  }

  override def handle(command: AddPlayerCmd): Unit = {
    import scala.concurrent.duration._
    import scala.language.postfixOps
    assert(gameRepositoryPort.exists(command.gameIdentity), s"GameIdentity ${command.gameIdentity} doesn't exist")
    val game = Await.result(gameRepositoryPort.findBurracoGameWaitingPlayersBy(command.gameIdentity), 5000 millis)
    gameRepositoryPort.save(game.addPlayer(command.playerToAdd))
  }

}