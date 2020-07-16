package com.abaddon83.utils.es

/**
 * Interface for a simple Repository, supporting version-based concurrency control
 */
interface Repository<T,A: AggregateRoot<T>> {
    fun getById(id: T): A?
    fun save(aggregate: A, expectedVersion: Long? = null) : Boolean
    fun new(id: T) : A // Delegating the creation of a new Aggregate to the Repository, for simplicity
}
