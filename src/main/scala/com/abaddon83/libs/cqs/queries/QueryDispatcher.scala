package com.abaddon83.libs.cqs.queries

import scala.concurrent.Future

trait QueryDispatcher {

  def dispatchAsync[TQuery,TQueryResult](query: TQuery): Future[TQueryResult]

  def dispatch[TQuery,TQueryResult](query: TQuery): TQueryResult

}
