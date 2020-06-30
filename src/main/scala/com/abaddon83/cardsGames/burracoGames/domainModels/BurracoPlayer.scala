package com.abaddon83.cardsGames.burracoGames.domainModels

import com.abaddon83.cardsGames.shares.players.{Player, PlayerIdentity}

trait BurracoPlayer extends Player{
}

case class PlayerNotAssigned(override val playerIdentity: PlayerIdentity) extends BurracoPlayer {

}