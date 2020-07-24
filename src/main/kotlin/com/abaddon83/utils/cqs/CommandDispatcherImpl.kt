package com.abaddon83.utils.cqs

import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandDispatcher
import org.koin.core.KoinComponent


class CommandDispatcherImpl(context: Context): CommandDispatcher, KoinComponent {

    private val _context: Context = context

    override fun <TCommand : Command> dispatch(command: TCommand) {
        return _context.resolveCommandHandler(command.javaClass).handle(command)
    }

    override suspend fun <TCommand : Command> dispatchAsync(command: TCommand) {
        return _context.resolveCommandHandler(command.javaClass).handleAsync(command)
    }

}