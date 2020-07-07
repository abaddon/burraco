package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.{FlowShape, OverflowStrategy}
import akka.stream.scaladsl._
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets.events.{ChatMessage, GameEvent, IncomingMessage, PlayerJoined, PlayerLeft}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class GameRoom(gameIdentity: GameIdentity)(implicit actorSystem: ActorSystem) {


  //Source.

  //val chatSource = Source.actorRef[ChatMessage](10, OverflowStrategy.fail)

  //val source = Source.actorRef[ChatMessage](bufferSize = 5, OverflowStrategy.fail)

  def websocketFlow(playerIdentity: PlayerIdentity, bufferSize: Int = 5, overflowStrategy: OverflowStrategy = OverflowStrategy.fail): Flow[Message, Message, _] = {

    //val gameRoomActor = actorSystem.actorOf(Props(classOf[GameRoomActor], gameIdentity))
    val gameRoomActor = actorSystem.actorOf(GameRoomActor.props(gameIdentity))

    //val source1 = Source.actorRef[ChatMessage](bufferSize = 5, OverflowStrategy.fail)

    val source: Source[Message, ActorRef] = Source.actorRef[ChatMessage](bufferSize, overflowStrategy)
      .map(msg => TextMessage(msg.text))


    Flow.fromGraph(GraphDSL.create(source) {
      implicit builder => {
        (chatSource) =>
          import GraphDSL.Implicits._

          val toActor = builder.add(Sink.actorRef(gameRoomActor, PoisonPill))

          //flow used as input, it takes Messages
          val fromWebsocket = builder.add(
            Flow[Message].collect {
              case TextMessage.Strict(txt) => IncomingMessage(playerIdentity, txt)
            })

          //flow used as output, it returns Messages
          val backToWebsocket = builder.add(
            Flow[ChatMessage].map {
              case ChatMessage(author, text) => TextMessage(s"[$author]: $text")
            }
          )

          //merges both pipes
          val merge = builder.add(Merge[Any](2))

          //Materialized value of Actor who sits in the chatroom
          //If Source actor is just created, it should be sent as UserJoined and registered as particiant in the room
          builder.materializedValue ~> Flow[ActorRef].map(actor => PlayerJoined(playerIdentity, actor)) ~> merge.in(1)
          //val actorAsSource = builder.materializedValue.map(actor => PlayerJoined(playerIdentity, actor))

          //Message from websocket is converted into IncommingMessage and should be sent to everyone in the room
          fromWebsocket ~> merge.in(0)

          //Actor already sits in chatRoom so each message from room is used as source and pushed back into the websocket
          //chatSource ~> backToWebsocket

          //Merges both pipes above and forwards messages to chatroom represented by ChatRoomActor
          merge ~> toActor

          FlowShape.of(fromWebsocket.in, chatSource.out)
      }
    })
}

  //def sendMessage (message: String /*ChatMessage*/ ): Unit = gameRoomActor ! message
}

//object GameRoom{
//  def apply(gameIdentity: GameIdentity)(implicit actorSystem: ActorSystem) = new GameRoom(gameIdentity, actorSystem)
//}