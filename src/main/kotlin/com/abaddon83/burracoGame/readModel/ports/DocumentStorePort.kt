package com.abaddon83.burracoGame.readModel.ports

interface DocumentStorePort<D> {
    fun save(key: String, document: D)
    fun get(key: String) : D?
}