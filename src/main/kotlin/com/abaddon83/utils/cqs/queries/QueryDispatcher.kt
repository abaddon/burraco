package com.abaddon83.utils.cqs.queries

interface QueryDispatcher{

    fun <TQuery : Query<TResponse>, TResponse> dispatch(query: TQuery): TResponse

    suspend fun <TQuery : Query<TResponse>, TResponse> dispatchAsync(query: TQuery): TResponse
}