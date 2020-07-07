package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets.events.{ChatMessage, IncomingMessage, PlayerJoined, PlayerLeft, SystemMessage}
import com.abaddon83.cardsGames.shares.games.GameIdentity
import com.abaddon83.cardsGames.shares.players.PlayerIdentity


object GameRoomActor {
  def props(gameIdentity: GameIdentity) = Props(new GameRoomActor(gameIdentity))

}

class GameRoomActor(gameIdentity: GameIdentity) extends Actor with ActorLogging {
  var players: Map[PlayerIdentity, ActorRef] = Map.empty[PlayerIdentity, ActorRef]

  override def receive: Receive = {
    case PlayerJoined(playerIdentity, actorRef) =>
      players += playerIdentity -> actorRef
      broadcast(SystemMessage(s"User $playerIdentity joined channel..."))
      println(s"User $playerIdentity joined channel[$gameIdentity]")

    case PlayerLeft(playerIdentity) =>
      println(s"User $playerIdentity left channel[$gameIdentity]")
      broadcast(SystemMessage(s"User $playerIdentity left channel[$gameIdentity]"))
      players -= playerIdentity

    case msg: IncomingMessage =>
      broadcast(ChatMessage(msg.playerIdentity.toString(),msg.message))
  }

  def broadcast(message: ChatMessage): Unit = players.values.foreach(_ ! message)
}

