package com.abaddon83.cardsGames.burracoGames.queries

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.queries.{Query, QueryHandler}

import scala.concurrent.Future

case class FindBurracoGameWaitingQuery(
                                        gameIdentity: GameIdentity
                                      ) extends Query {

}

class FindBurracoGameWaitingHandler(
                                     gameRepositoryPort: GameRepositoryPort
                                   ) extends QueryHandler[FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers] {
  override def handleAsync(query: FindBurracoGameWaitingQuery): Future[BurracoGameWaitingPlayers] = {
    gameRepositoryPort.findBurracoGameWaitingPlayersBy(query.gameIdentity)
  }

  override def handle(query: FindBurracoGameWaitingQuery): BurracoGameWaitingPlayers = ???
}
