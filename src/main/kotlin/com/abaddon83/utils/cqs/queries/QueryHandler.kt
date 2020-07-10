package com.abaddon83.utils.cqs.queries

interface QueryHandler<TQuery : Query<TResponse>, TResponse> {

    fun handle(query: TQuery): TResponse

    suspend fun handleAsync(query: TQuery): TResponse

}