package com.abaddon83.cardsGames.shares.games

object GameTypes {

  val fullGameType: Seq[GameType] = Seq(Burraco)

  sealed trait GameType {
    val label: String
  }

  case object Burraco extends GameType {
    override val label: String = "A"
  }

}