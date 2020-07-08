package com.abaddon83.cardsGames.burracoGames.iocs
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameRepositoryAdapters.BurracoGameRepositoryFakeAdapter
import com.abaddon83.cardsGames.burracoGames.adapters.PlayerAdapters.PlayerFakeAdapter
import com.abaddon83.cardsGames.burracoGames.ports.{BurracoGameRepositoryPort, PlayerPort}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.InjectorExtensions._
import net.codingwell.scalaguice.ScalaModule

class PortsModule()(implicit val ec: scala.concurrent.ExecutionContext) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[BurracoGameRepositoryPort].toInstance(new BurracoGameRepositoryFakeAdapter())
    bind[PlayerPort].toInstance(new PlayerFakeAdapter())
  }
}
