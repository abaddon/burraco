package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters

import akka.actor.{Actor, ActorRef}

class PlayerActor extends Actor {

  private var connections: List[ActorRef] = List()

  override def receive: Receive = {

    case message: Protocol.OpenConnection => {
      //this.connections = message.connection :: this.connections
      //message.connection ! Protocol.ConnectionEstablished()
    }

    case message: Protocol.CloseConnection => {
      // how can I remove actor from this.connections ?
    }

    case message: Protocol.newGame => {
      // how can I identify from which connection this message came in?
    }
  }
}
