package com.abaddon83.cardsGames

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.settings.ServerSettings
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.BurracoGameRestRoute
import com.abaddon83.cardsGames.burracoGames.commands.GameCommandDispatcher
import com.abaddon83.cardsGames.burracoGames.iocs.{CommandModule, PortsModule, QueryModule}
import com.abaddon83.cardsGames.burracoGames.ports.PlayerPort
import com.abaddon83.cardsGames.burracoGames.queries.GameQueryDispatcher
import com.abaddon83.cardsGames.burracoGames.services.BurracoGameService
import com.abaddon83.libs.cqs.Context
import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._

import scala.io.StdIn
import scala.util.Try

trait AkkaHttpServer{
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  lazy val logger = Logging(system, classOf[App])

  val host: String = "127.0.0.1"
  val port: Int = Try(System.getenv("PORT")).map(_.toInt).getOrElse(9000)



  val injector = Guice.createInjector(new PortsModule())
  val CQSInjector = injector.createChildInjector(new QueryModule(injector), new CommandModule(injector))

  val burracoGameService = new BurracoGameService(
    playerPort = injector.instance[PlayerPort],
    commandDispatcher = new GameCommandDispatcher(context = new Context(CQSInjector)),
    queryDispatcher = new GameQueryDispatcher (context = new Context(CQSInjector))
  )

  val restRoute = new BurracoGameRestRoute(burracoGameService)
  //val wsRoute = new BurracoGameWebSocketRoute(burracoGameService)

  lazy val routes: Route = /*handleExceptions(globalExceptionHandler)*/{
    pathPrefix("api") {
      concat(
        restRoute.route,
    //    wsRoute.route
      )
    }
  }


  //val burracoGameRoutes = new BurracoGameUIAkkaRoutes()

  val defaultSettings = ServerSettings(system)
  val pingCounter = new AtomicInteger()
  val customWebsocketSettings =
    defaultSettings.websocketSettings
      .withPeriodicKeepAliveData(() => ByteString(s"debug-${pingCounter.incrementAndGet()}"))
  val customServerSettings =
    defaultSettings.withWebsocketSettings(customWebsocketSettings)

  def startServer() = {
    val bindingFuture = Http().bindAndHandle(routes, host, port, settings = customServerSettings)
    logger.info(s"Starting the HTTP server at ${port} press return to stop")
    StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }

}