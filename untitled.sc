import com.abaddon83.burraco.shares.decks.{Deck, Ranks}

(Deck.allRanksCards()++Deck.allRanksCards()).filter(c=>c.rank == Ranks.Queen)