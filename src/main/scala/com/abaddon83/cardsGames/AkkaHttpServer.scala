package com.abaddon83.cardsGames

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage, WebSocketRequest}
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.settings.ServerSettings
import akka.pattern.ask
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Flow, GraphDSL, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, FlowShape, OverflowStrategy}
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success, Try}
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.BurracoGameRestRoute
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets.BurracoGameWebSocketRoute

trait AkkaHttpServer{



  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  lazy val logger = Logging(system, classOf[App])

  val host: String = "127.0.0.1"
  val port: Int = Try(System.getenv("PORT")).map(_.toInt).getOrElse(9000)

  val restRoute = new BurracoGameRestRoute()
  val wsRoute = new BurracoGameWebSocketRoute()

  lazy val routes: Route = /*handleExceptions(globalExceptionHandler)*/{
    pathPrefix("api") {
      concat(
        restRoute.route,
        wsRoute.route
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

//object Route {
//
//  case object GetWebsocketFlow
//
//  implicit val as = ActorSystem("example")
//  implicit val am = ActorMaterializer()
//
//  val websocketRoute =
//    pathEndOrSingleSlash {
//      complete("WS server is alive\n")
//    } ~ path("connect") {
//      //handleWebSocketMessages()
//      val handler = as.actorOf(Props[ClientHandlerActor])
//      val futureFlow = (handler ? GetWebsocketFlow) (3.seconds).mapTo[Flow[Message, Message, _]]
//
//      onComplete(futureFlow) {
//        case Success(flow) => handleWebSocketMessages(flow)
//        case Failure(err) => complete(err.toString)
//      }
//
//    }
//}

//class ClientHandlerActor extends Actor {
//
//  implicit val as = context.system
//  implicit val am = ActorMaterializer()
//
//  val (down, publisher) = Source
//    .actorRef[String](1000, OverflowStrategy.fail)
//    .toMat(Sink.asPublisher(fanout = false))(Keep.both)
//    .run()
//
//  // test
//  var counter = 0
//  as.scheduler.schedule(0.seconds, 0.5.second, new Runnable {
//    override def run() = {
//      counter = counter + 1
//      self ! counter
//    }
//  })
//
//  override def receive = {
//    case GetWebsocketFlow =>
//
//      val flow = Flow.fromGraph(GraphDSL.create() { implicit b =>
//        val textMsgFlow = b.add(Flow[Message]
//          .mapAsync(1) {
//            case tm: TextMessage => tm.toStrict(3.seconds).map(_.text)
//            case bm: BinaryMessage =>
//              // consume the stream
//              bm.dataStream.runWith(Sink.ignore)
//              Future.failed(new Exception("yuck"))
//          })
//
//        val pubSrc = b.add(Source.fromPublisher(publisher).map(TextMessage(_)))
//
//        textMsgFlow ~> Sink.foreach[String](self ! _)
//        FlowShape(textMsgFlow.in, pubSrc.out)
//      })
//
//      sender ! flow
//
//    // replies with "hello XXX"
//    case s: String =>
//      println(s"client actor received $s")
//      down ! "Hello " + s + "!"
//
//    // passes any int down the websocket
//    case n: Int =>
//      println(s"client actor received $n")
//      down ! n.toString
//  }
//}
