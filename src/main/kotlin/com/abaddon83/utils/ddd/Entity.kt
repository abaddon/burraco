package com.abaddon83.utils.ddd

import com.abaddon83.utils.logs.WithLog

abstract class Entity<T>: WithLog() {
    protected abstract val identity: T

    fun identity(): T = identity
}