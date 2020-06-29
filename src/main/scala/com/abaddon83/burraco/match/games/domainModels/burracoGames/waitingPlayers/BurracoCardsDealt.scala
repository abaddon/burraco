package com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.mazzettos.MazzettoDeck
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoDeck, DiscardPile}
import com.abaddon83.burraco.shares.decks.Card
import com.abaddon83.burraco.shares.players.PlayerIdentity

case class BurracoCardsDealt(
                              firstPozzettoDeck: MazzettoDeck,
                              secondPozzettoDeck: MazzettoDeck,
                              playersCards: Map[PlayerIdentity,List[Card]],
                              discardPile : DiscardPile,
                              burracoDeck : BurracoDeck
                            )
