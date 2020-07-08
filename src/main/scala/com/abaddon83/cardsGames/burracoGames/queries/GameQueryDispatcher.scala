package com.abaddon83.cardsGames.burracoGames.queries

import com.abaddon83.libs.cqs.Context
import com.abaddon83.libs.cqs.commands.{CommandDispatcher, CommandHandler}
import com.abaddon83.libs.cqs.queries.{QueryDispatcher, QueryHandler}

import scala.concurrent.Future

class GameQueryDispatcher(context: Context) extends QueryDispatcher{

  override def dispatchAsync[TQuery, TQueryResult](query: TQuery): Future[TQueryResult] = {
  val handler = context.resolve[QueryHandler[TQuery, TQueryResult]]
      handler.handleAsync(query)
}

  override def dispatch[TQuery, TQueryResult](query: TQuery): TQueryResult = {
    val handler = context.resolve[QueryHandler[TQuery, TQueryResult]]
    handler.handle(query)
  }
}
