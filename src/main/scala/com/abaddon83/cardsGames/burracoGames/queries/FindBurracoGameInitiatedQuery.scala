package com.abaddon83.cardsGames.burracoGames.queries

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnExecution}
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.queries.{Query, QueryHandler}

import scala.concurrent.Future

case class FindBurracoGameInitiatedQuery(
                                        gameIdentity: GameIdentity
                                      ) extends Query {
}

class FindBurracoGameInitiatedHandler(
                                     gameRepositoryPort: GameRepositoryPort
                                   ) extends QueryHandler[FindBurracoGameInitiatedQuery, BurracoGameInitiated] {
  override def handleAsync(query: FindBurracoGameInitiatedQuery): Future[BurracoGameInitiated] = {
    gameRepositoryPort.findBurracoGameInitialisedBy(query.gameIdentity)
  }

  override def handle(query: FindBurracoGameInitiatedQuery): BurracoGameInitiated = ???
}
