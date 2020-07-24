package com.abaddon83.utils.cqs

import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryDispatcher
import org.koin.core.KoinComponent

class QueryDispatcherImpl(context: Context): QueryDispatcher, KoinComponent {

    private val _context: Context=context

    override fun <TQuery : Query<TResponse>, TResponse> dispatch(query: TQuery): TResponse {
        return _context.resolveQueryHandler(query.javaClass).handle(query)
    }

    override suspend fun <TQuery : Query<TResponse>, TResponse> dispatchAsync(query: TQuery): TResponse {
        return _context.resolveQueryHandler(query.javaClass).handleAsync(query)
    }
}