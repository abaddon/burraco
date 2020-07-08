package com.abaddon83.cardsGames.burracoGames.queries

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiatedTurnExecution}
import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.queries.{Query, QueryHandler}

import scala.concurrent.Future

case class FindBurracoGameInitiatedTurnExecQuery(
                                        gameIdentity: GameIdentity
                                      ) extends Query {
}

class FindBurracoGameInitiatedTurnExecHandler(
                                     gameRepositoryPort: BurracoGameRepositoryPort
                                   ) extends QueryHandler[FindBurracoGameInitiatedTurnExecQuery, BurracoGameInitiatedTurnExecution] {
  override def handleAsync(query: FindBurracoGameInitiatedTurnExecQuery): Future[BurracoGameInitiatedTurnExecution] = {
    gameRepositoryPort.findBurracoGameInitialisedTurnExecutionBy(query.gameIdentity)
  }

  override def handle(query: FindBurracoGameInitiatedTurnExecQuery): BurracoGameInitiatedTurnExecution = ???
}
