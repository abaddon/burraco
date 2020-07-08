package com.abaddon83.cardsGames.burracoGames.queries

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameRepositoryPort
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.libs.cqs.queries.{Query, QueryHandler}

import scala.concurrent.Future

case class FindBurracoGameStartedTurnStartQuery(
                                        gameIdentity: GameIdentity
                                      ) extends Query {

}

class FindBurracoGameStartedTurnStartHandler(
                                     gameRepositoryPort: BurracoGameRepositoryPort
                                   ) extends QueryHandler[FindBurracoGameStartedTurnStartQuery, BurracoGameInitiatedTurnStart] {
  override def handleAsync(query: FindBurracoGameStartedTurnStartQuery): Future[BurracoGameInitiatedTurnStart] = {
    gameRepositoryPort.findBurracoGameInitialisedTurnStartBy(query.gameIdentity)
  }

  override def handle(query: FindBurracoGameStartedTurnStartQuery): BurracoGameInitiatedTurnStart = ???
}
