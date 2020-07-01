package com.abaddon83.cardsGames.burracoGames.queries

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution}
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.queries.{Query, QueryHandler}

import scala.concurrent.Future

case class FindBurracoGameInitiatedTurnEndQuery(
                                        gameIdentity: GameIdentity
                                      ) extends Query {
}

class FindBurracoGameInitiatedTurnEndHandler(
                                     gameRepositoryPort: GameRepositoryPort
                                   ) extends QueryHandler[FindBurracoGameEndedQuery, BurracoGameInitiatedTurnEnd] {
  override def handleAsync(query: FindBurracoGameEndedQuery): Future[BurracoGameInitiatedTurnEnd] = {
    gameRepositoryPort.findBurracoGameInitialisedTurnEndBy(query.gameIdentity)
  }

  override def handle(query: FindBurracoGameEndedQuery): BurracoGameInitiatedTurnEnd = ???
}
