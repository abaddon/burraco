package com.abaddon83.burracoGame.ports

import com.abaddon83.utils.es.AggregateRoot
import com.abaddon83.utils.es.repository.Repository

interface BurracoGameEventStorePort<T,A>: Repository<T,AggregateRoot<T>> {
}