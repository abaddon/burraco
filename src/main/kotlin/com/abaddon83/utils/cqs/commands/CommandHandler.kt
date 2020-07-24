package com.abaddon83.utils.cqs.commands

interface CommandHandler<TCommand : Command> {

    fun handle(command: TCommand)

    suspend fun handleAsync(command: TCommand)
}