package com.abaddon83.cardsGames.burracoGames.iocs

import com.abaddon83.cardsGames.mocks.MockBurracoGameRepositoryAdapter
import com.abaddon83.libs.cqs.Context
import com.google.inject.Guice
import org.scalatest.funsuite.AnyFunSuite

class CQSModuleTest extends AnyFunSuite with MockBurracoGameRepositoryAdapter{

  test("init") {
    val injector = Guice.createInjector(new CQSModule(mockBurracoGameRepositoryAdapter))

    val context = new Context(injector)
    println(context.getInjector.getAllBindings.keySet())
  }
}
