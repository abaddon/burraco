package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets.events

import akka.actor.ActorRef
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class ChatMessage(sender: String, text: String)

object SystemMessage {
  def apply(text: String) = ChatMessage("System", text)
}


sealed trait GameEvent

case class PlayerJoined(playerIdentity: PlayerIdentity, userActor: ActorRef) extends GameEvent

case class PlayerLeft(playerIdentity: PlayerIdentity) extends GameEvent

case class IncomingMessage(playerIdentity: PlayerIdentity, message: String) extends GameEvent
