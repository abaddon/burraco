package com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers

import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoDeck, DiscardPile}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.mazzettos.MazzettoDeck
import com.abaddon83.cardsGames.shares.decks.Card
import com.abaddon83.cardsGames.shares.players.PlayerIdentity

case class BurracoCardsDealt(
                              firstPozzettoDeck: MazzettoDeck,
                              secondPozzettoDeck: MazzettoDeck,
                              playersCards: Map[PlayerIdentity,List[Card]],
                              discardPile : DiscardPile,
                              burracoDeck : BurracoDeck
                            )
