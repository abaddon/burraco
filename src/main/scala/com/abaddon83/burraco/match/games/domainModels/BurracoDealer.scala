package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoDealer private(burracoDeck: BurracoDeck, burracoGame: BurracoGameWaitingPlayers){
  private val numCardsForPlayer = 11
  private val numCardsPozzetto:Array[Int] = Array(11,18)

  def dealCardsToPlayers(): Map[PlayerIdentity,List[Card]] ={
    burracoGame.players.map(
      player => Map(player.playerIdentity -> dealCardsToPlayer())
    ).reduce(_++_)
  }

  def dealCardsToFirstPozzetto(): PozzettoDeck = {
    PozzettoDeck(grabCards(numCardsPozzetto(burracoGame.players.size%2)))
  }

  def dealCardsToSecondPozzetto(): PozzettoDeck = {
    PozzettoDeck(grabCards(numCardsPozzetto(0)))
  }

  def dealCardToDiscardPile(): DiscardPile = {
    DiscardPile(grabCards(1))
  }

  private def dealCardsToPlayer() : List[Card] = {
    grabCards(numCardsForPlayer)
  }

  private def grabCards(numCards: Int): List[Card] = {
    (1 to numCards).map(_ =>burracoDeck.grabFirstCard()).toList
  }
}

object BurracoDealer{
  def apply(burracoGame: BurracoGameWaitingPlayers): BurracoDealer = {
    val deckShuffled = deckShuffle(BurracoDeck())
    new BurracoDealer(deckShuffled,burracoGame)
  }

  private def deckShuffle(deck: BurracoDeck): BurracoDeck ={
    BurracoDeck(scala.util.Random.shuffle(deck.cards))
  }

}





