package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.players.{Player, PlayerIdentity}

case class BurracoPlayer(playerIdentity: PlayerIdentity) extends Player{
}

@deprecated
case class PlayerNotAssigned(override val playerIdentity: PlayerIdentity) extends BurracoPlayer(playerIdentity) {

}

@deprecated
case class BurracoPlayerAssigned(override val playerIdentity: PlayerIdentity) extends BurracoPlayer(playerIdentity) {

}