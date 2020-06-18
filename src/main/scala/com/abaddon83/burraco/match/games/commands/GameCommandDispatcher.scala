package com.abaddon83.burraco.`match`.games.commands

import com.abaddon83.libs.cqs.Context
import com.abaddon83.libs.cqs.commands.{CommandDispatcher, CommandHandler}

import scala.concurrent.Future

class GameCommandDispatcher(context: Context) extends CommandDispatcher{
  override def dispatchAsync[TCommand](command: TCommand): Future[Unit] = {
    val handler = context.resolve[CommandHandler[TCommand]]
    handler.handleAsync(command)
  }

  override def dispatch[TCommand](command: TCommand): Unit = {
    val handler = context.resolve[CommandHandler[TCommand]]
    handler.handle(command)
  }
}
