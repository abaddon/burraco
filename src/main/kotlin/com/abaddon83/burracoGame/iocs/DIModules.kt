package com.abaddon83.burracoGame.iocs

import com.abaddon83.burracoGame.controller.adapter.BurracoGameControllerRestAdapter
import com.abaddon83.burracoGame.readModel.adapters.burracoGameReadModelRepositoryAdapters.BurracoGameRepositoryInMemoryAdapter
import com.abaddon83.burracoGame.commandModel.adapters.playerAdapters.PlayerInMemoryAdapter
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameCommandControllerPort
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelRepositoryPort
import com.abaddon83.burracoGame.commandModel.ports.PlayerPort
import com.abaddon83.burracoGame.readModel.ports.BurracoGameReadModelControllerPort
import com.abaddon83.burracoGame.services.BurracoGameService
import com.abaddon83.utils.cqs.Context
import com.abaddon83.utils.cqs.ContextImpl
import org.koin.dsl.module

val AppAdapters = module {
    val cqsPackages = listOf("com.abaddon83.burracoGame.commands","com.abaddon83.burracoGame.readModel.queries")

    single<Context> { ContextImpl(cqsPackages) }
    single<BurracoGameReadModelRepositoryPort> { BurracoGameRepositoryInMemoryAdapter() }
    single<PlayerPort> { PlayerInMemoryAdapter() }
    single<BurracoGameReadModelControllerPort> { BurracoGameControllerRestAdapter(BurracoGameService()) }
    single<BurracoGameCommandControllerPort> { BurracoGameControllerRestAdapter(BurracoGameService()) }
}