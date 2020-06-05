package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.{Player, PlayerIdentity}

import scala.collection.mutable.ListBuffer

trait BurracoPlayer extends Player{
}


case class PlayerNotAssigned(playerIdentity: PlayerIdentity) extends BurracoPlayer {

}

case class BurracoPlayerAssigned(playerIdentity: PlayerIdentity) extends BurracoPlayer {

}

case class BurracoPlayerInGame(playerIdentity: PlayerIdentity, cards: ListBuffer[Card]) extends BurracoPlayer {

}

object BurracoPlayerInGame{
  def apply(playerIdentity: PlayerIdentity, cards: List[Card]): BurracoPlayerInGame = new BurracoPlayerInGame(playerIdentity, cards.to(ListBuffer))
}