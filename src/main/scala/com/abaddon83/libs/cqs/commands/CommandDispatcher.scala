package com.abaddon83.libs.cqs.commands

import scala.concurrent.Future

trait CommandDispatcher {

  def dispatchAsync[TCommand](command: TCommand): Future[Unit]

  def dispatch[TCommand](command: TCommand): Unit

}
