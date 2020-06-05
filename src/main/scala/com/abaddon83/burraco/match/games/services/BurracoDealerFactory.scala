package com.abaddon83.burraco.`match`.games.services

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoCardsDealt, BurracoDeck, BurracoGameWaitingPlayers, DiscardPile, PozzettoDeck}
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity




case class BurracoDealerFactory private(private val burracoDeck: BurracoDeck, private val burracoGame: BurracoGameWaitingPlayers){
  private val numCardsForPlayer = 11
  private val numCardsForDiscardPile = 1
  private val numCardsPozzetto:Array[Int] = Array(11,18)


  def dealBurracoCards(): BurracoCardsDealt = {
    BurracoCardsDealt(
      dealCardsToFirstPozzetto(),
      dealCardsToSecondPozzetto(),
      dealCardsToPlayers(),
      dealCardToDiscardPile(),
      burracoDeck
    )
  }

  private def dealCardsToPlayers(): Map[PlayerIdentity,List[Card]] ={
    burracoGame.players.map(
      player => dealCardsToPlayer(player.playerIdentity)
    ).reduce(_++_)
  }

  private def dealCardsToFirstPozzetto(): PozzettoDeck = {
    PozzettoDeck(grabCards(numCardsPozzetto(burracoGame.players.size%2)))
  }

  private def dealCardsToSecondPozzetto(): PozzettoDeck = {
    PozzettoDeck(grabCards(numCardsPozzetto(0)))
  }

  private def dealCardToDiscardPile(): DiscardPile = {
    DiscardPile(grabCards(numCardsForDiscardPile))
  }

  private def dealCardsToPlayer(playerIdentity: PlayerIdentity) : Map[PlayerIdentity,List[Card]] = {
    Map(playerIdentity -> grabCards(numCardsForPlayer))
  }

  private def grabCards(numCards: Int): List[Card] = {
    (1 to numCards).map(_ =>burracoDeck.grabFirstCard()).toList
  }
}

object BurracoDealerFactory{
  def apply(burracoGame: BurracoGameWaitingPlayers): BurracoDealerFactory = {
    val deckShuffled = deckShuffle(BurracoDeck())
    new BurracoDealerFactory(deckShuffled,burracoGame)
  }

  private def deckShuffle(deck: BurracoDeck): BurracoDeck ={
    deck.shuffle()
  }

}





