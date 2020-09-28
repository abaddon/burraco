package com.abaddon83.burracoGame.writeModel.models.decks

import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentityCustomSerializer
import kotlinx.serialization.json.Json
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class CardTest {

    @Test
    fun `given a Card when I serialise it, then I should have the same Card deserialized`() {

        val card: Card = Card(suit = Suits.Clover, rank = Ranks.Ace);

        val jsonString = Json.encodeToString(CardCustomSerializer,card);
        val deserializedCard = Json.decodeFromString<Card>(CardCustomSerializer,jsonString)
        assertEquals(card.suit,deserializedCard.suit)
        assertEquals(card.rank,deserializedCard.rank)
    }
}