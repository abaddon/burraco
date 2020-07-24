package com.abaddon83.utils.cqs.commands

interface CommandDispatcher{
    fun <TCommand: Command> dispatch(command: TCommand): Unit

    suspend fun  <TCommand: Command> dispatchAsync(command: TCommand): Unit
}