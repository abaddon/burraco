package com.abaddon83.libs.cqs.queries

import scala.concurrent.Future

trait QueryDispatcher[TQuery,TQueryResult] {

  def dispatchAsync(query: TQuery): Future[TQueryResult]

  def dispatch(query: TQuery): TQueryResult

}
