package com.abaddon83.cardsGames.burracoGames.commands

import com.abaddon83.libs.cqs.Context
import com.abaddon83.libs.cqs.commands.{CommandDispatcher, CommandHandler}

import scala.concurrent.Future

class GameCommandDispatcher(context: Context) extends CommandDispatcher{
//  import net.codingwell.scalaguice.InjectorExtensions._

  override def dispatchAsync[TCommand](command: TCommand): Future[Unit] = {
    context.func2(command)
    val handler = context.resolve[CommandHandler[TCommand]]
    //val handler = context.getInjector.instance[CommandHandler[command.type ]]
    handler.handleAsync(command)
  }

  override def dispatch[TCommand](command: TCommand): Unit = {
    //val handler = context.getInjector.instance[CommandHandler[command.type]]
    val handler = context.resolve[CommandHandler[TCommand]]
    handler.handle(command)
  }
}
