package com.abaddon83.burraco.`match`.games.domainModels

import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoCardsDealt(
                              firstPozzettoDeck: PozzettoDeck,
                              secondPozzettoDeck: PozzettoDeck,
                              playersCards: Map[PlayerIdentity,List[Card]],
                              discardPile : DiscardPile,
                              burracoDeck : BurracoDeck
                            )
