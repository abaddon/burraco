package com.abaddon83.cardsGames.burracoGames.queries

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.ended.BurracoGameEnded
import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.queries.{Query, QueryHandler}

import scala.concurrent.Future

case class FindBurracoGameEndedQuery(
                                        gameIdentity: GameIdentity
                                      ) extends Query {
}

class FindBurracoGameEndedHandler(
                                     gameRepositoryPort: BurracoGameRepositoryPort
                                   ) extends QueryHandler[FindBurracoGameEndedQuery, BurracoGameEnded] {
  override def handleAsync(query: FindBurracoGameEndedQuery): Future[BurracoGameEnded] = {
    gameRepositoryPort.findBurracoBurracoGameEndedBy(query.gameIdentity)
  }

  override def handle(query: FindBurracoGameEndedQuery): BurracoGameEnded = ???
}
