package com.abaddon83.burraco.shares.games

object GameTypes {

  val fullGameType: Seq[GameType] = Seq(Burraco)

  sealed trait GameType {
    val label: String
  }

  case object Burraco extends GameType {
    override val label: String = "A"
  }

}