package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.webSockets

import akka.actor.ActorSystem
import com.abaddon83.cardsGames.shares.games.GameIdentity

object GameRooms {
  var gameRooms: Map[GameIdentity, GameRoom] = Map.empty[GameIdentity, GameRoom]
  def findOrCreate(gameIdentity: GameIdentity)(implicit actorSystem: ActorSystem): GameRoom = {
    gameRooms.getOrElse(gameIdentity, createNewGameRoom(gameIdentity))
  }

  private def createNewGameRoom(gameIdentity: GameIdentity)(implicit actorSystem: ActorSystem): GameRoom = {
    val gameRoom = new GameRoom(gameIdentity)
    gameRooms += gameIdentity -> gameRoom
    gameRoom
  }
}