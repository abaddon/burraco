package com.abaddon83.cardsGames.burracoGames.queries

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.queries.{Query, QueryHandler}

import scala.concurrent.Future

case class FindBurracoGameQuery(
                                        gameIdentity: GameIdentity
                                      ) extends Query {

}

class FindBurracoGameHandler(
                                     gameRepositoryPort: BurracoGameRepositoryPort
                                   ) extends QueryHandler[FindBurracoGameQuery, BurracoGame] {
  override def handleAsync(query: FindBurracoGameQuery): Future[BurracoGame] = {
    gameRepositoryPort.findBurracoGameBy(query.gameIdentity)
  }

  override def handle(query: FindBurracoGameQuery): BurracoGame = ???
}
