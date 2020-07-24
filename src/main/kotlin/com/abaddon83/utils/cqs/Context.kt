package com.abaddon83.utils.cqs

import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler

interface Context {
    fun <TCommand : Command> resolveCommandHandler(command: Class<TCommand>): CommandHandler<TCommand>

    fun <TQuery : Query<TResult>, TResult> resolveQueryHandler(classOfQuery: Class<TQuery>): QueryHandler<TQuery, TResult>
}