package com.abaddon83.libs.cqs.queries

import scala.concurrent.Future

trait QueryHandler[TQuery,TQueryResult]{

  def handleAsync( query: TQuery): Future[TQueryResult]
  def handle( query: TQuery): TQueryResult

}
