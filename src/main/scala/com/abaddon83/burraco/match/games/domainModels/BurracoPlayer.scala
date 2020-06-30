package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.players.{Player, PlayerIdentity}

trait BurracoPlayer extends Player{
}

case class PlayerNotAssigned(override val playerIdentity: PlayerIdentity) extends BurracoPlayer {

}