package com.abaddon83.burracoGame.iocs

import com.abaddon83.utils.cqs.Context
import com.abaddon83.utils.cqs.ContextImpl
import org.koin.dsl.module

val AppAdapters = module {
    val cqsPackages = listOf("com.abaddon83.burracoGame.commands","com.abaddon83.burracoGame.queries")

    single<Context> { ContextImpl(cqsPackages) }
    //single<MarvelCharacterServicePort> { MarvelCharacterServiceRestAdapter() }
    //single<TranslateServicePort> { TranslateServiceGoogleAdapter() }
    //single<CharacterRepositoryPort> { CharacterRepositoriesInMemoryAdapter() }
    //single<CharacterControllerPort> { RestCharacterControllerAdapter(CharacterService()) }
}