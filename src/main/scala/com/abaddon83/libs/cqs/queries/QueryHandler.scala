package com.abaddon83.libs.cqs.queries

import scala.concurrent.Future

trait QueryHandler[TQueryHandler,TQueryResult]{

  def handleAsync( query: TQueryHandler): Future[TQueryResult]
  def handle( query: TQueryHandler): TQueryResult

}
