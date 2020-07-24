package com.abaddon83.utils.es.repository

import com.abaddon83.utils.es.AggregateRoot

/**
 * Interface for a simple Repository, supporting version-based concurrency control
 */
interface Repository<T,A: AggregateRoot<T>> {
    fun getById(id: T): A?
    fun save(aggregate: A, expectedVersion: Long? = null) : A
    fun new(id: T) : A // Delegating the creation of a new Aggregate to the Repository, for simplicity
    fun exist(id: T): Boolean
}
