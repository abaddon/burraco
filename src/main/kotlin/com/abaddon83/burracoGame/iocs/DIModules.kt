package com.abaddon83.burracoGame.iocs

import com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.BurracoGameControllerRestAdapter
import com.abaddon83.burracoGame.adapters.burracoGameRepositoryAdapters.inMemories.BurracoGameRepositoryInMemoryAdapter
import com.abaddon83.burracoGame.adapters.playerAdapters.inMemories.PlayerInMemoryAdapter
import com.abaddon83.burracoGame.ports.BurracoGameControllerPort
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.ports.PlayerPort
import com.abaddon83.burracoGame.services.BurracoGameService
import com.abaddon83.utils.cqs.Context
import com.abaddon83.utils.cqs.ContextImpl
import org.koin.dsl.module

val AppAdapters = module {
    val cqsPackages = listOf("com.abaddon83.burracoGame.commands","com.abaddon83.burracoGame.queries")

    single<Context> { ContextImpl(cqsPackages) }
    single<BurracoGameRepositoryPort> { BurracoGameRepositoryInMemoryAdapter() }
    single<PlayerPort> { PlayerInMemoryAdapter() }
    single<BurracoGameControllerPort> { BurracoGameControllerRestAdapter(BurracoGameService()) }
}