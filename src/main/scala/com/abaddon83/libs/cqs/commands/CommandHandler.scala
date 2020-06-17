package com.abaddon83.libs.cqs.commands

import scala.concurrent.Future

trait CommandHandler[TCommand] {

  def handleAsync( command: TCommand): Future[Unit]
  def handle( command: TCommand): Unit

}
