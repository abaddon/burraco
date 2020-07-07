package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.{BurracoGameJsonSupport, BurracoGameUIAkkaAdapter, PlayerActor, Protocol}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity


class BurracoGameWebSocketRoute(/*burracoGameUIAkkaAdapter: BurracoGameUIAkkaAdapter*/)(implicit as: ActorSystem)
  extends Directives with BurracoGameJsonSupport{

  implicit val materializer = ActorMaterializer()

  case object GetWebsocketFlow

  val route: Route = {
    pathPrefix("ws") {
      extractUri { uri =>
        pathPrefix("game" / JavaUUID) { gameIdentityUUID =>
          parameter("player") { playerId =>

            val gameIdentity = GameIdentity(gameIdentityUUID)
            val playerIdentity = PlayerIdentity(playerId)
            println(s"gameIdentity: ${gameIdentity}")
            println(s"playerIdentity: ${playerIdentity}")
            //burracoGameUIAkkaAdapter.findBurracoGame(gameIdentity)
            handleWebSocketMessages(
              GameRooms.findOrCreate(gameIdentity).websocketFlow(playerIdentity)
            )


            //val futureFlow = (handler ? GetWebsocketFlow) (3.seconds).mapTo[Flow[Message, Message, _]]
            //onComplete(futureFlow) {
            //  case Success(flow) => handleWebSocketMessages(flow)
            //  case Failure(err) => complete(err.toString)
            //}
          }
        }
      }
    }
  }
}


//val echoService: Flow[Message, Message, _] = Flow[Message].map {
//  case TextMessage.Strict (txt) => TextMessage ("ECHO: " + txt)
//  case _ => TextMessage ("Message type unsupported")
//}
//
//
//  def websocketFlow (actorRef: ActorRef, flowId: String): Flow[Message, Message, Any] =
//  Flow[Message]
//  .map {
//  case TextMessage.Strict (textMessage) => protocol.hydrate (textMessage)
//}
//  .via (actorFlow (flowId) )
//  .map (event => TextMessage.Strict (protocol.serialize (event) ) )
//
//
//  def actorFlow (flowId: String): Flow[Protocol.Message, Protocol.Event, Any] = {
//  val sink =
//  Flow[Protocol.Message]
//  .to (Sink.actorRef[Protocol.Message] (actorRef, Protocol.CloseConnection () ) )
//
//  val source =
//  Source.actorRef[Protocol.Event] (1, OverflowStrategy.fail)
//  .mapMaterializedValue (actor => actorRef ! Protocol.OpenConnection (actor) )
//
//  Flow.fromSinkAndSource (sink, source)
//}
//}
