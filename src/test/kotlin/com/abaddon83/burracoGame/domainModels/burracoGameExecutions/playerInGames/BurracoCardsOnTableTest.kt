package com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames

import com.abaddon83.burracoGame.commandModel.models.BurracoScale
import com.abaddon83.burracoGame.commandModel.models.BurracoTris
import com.abaddon83.burracoGame.commandModel.models.burracoGameExecutions.playerInGames.BurracoCardsOnTable
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.decks.ListCardsBuilder
import com.abaddon83.burracoGame.commandModel.models.decks.Ranks
import com.abaddon83.burracoGame.commandModel.models.decks.Suits
import org.junit.Test
import kotlin.test.assertFailsWith

class BurracoCardsOnTableTest {

    @Test
    fun `given a tris not yet burraco and a scala not yet burraco when I ask the list of burraco, then I receive an empty list`() {
        val tris = createTris(false)
        val scale = createScale(false)

        val burracoCardsOnTable = BurracoCardsOnTable(
                listOfTris = listOf(tris),
                listOfScale = listOf(scale)
        )

        //println(s"tris: ${tris.identity()} - ${tris.showCards().size} - ${tris.isBurraco()}")
        //println(s"scale: ${scale.identity()} - ${scale.showCards().size} - ${scale.isBurraco()}")

        val expectedSize = 0

        assert(burracoCardsOnTable.burracoList().size == expectedSize)
    }

    @Test
    fun `given a tris burraco and a scala not yet burraco when I ask the list of burraco, then I receive a list with a burraco `() {
        val tris = createTris(true)
        val scale = createScale(false)

        val burracoCardsOnTable = BurracoCardsOnTable(
                listOfTris = listOf(tris),
                listOfScale = listOf(scale)
        )


        val expectedSize = 1
        val expectedId = tris.identity()
        val expectedTrisCards = tris.numCards()
        val expectedScaleCards = scale.numCards()

        assert(burracoCardsOnTable.burracoList().size == expectedSize)
        assert(burracoCardsOnTable.burracoList().first().identity() == expectedId)
        assert(burracoCardsOnTable.numCardsOnTable() == expectedTrisCards + expectedScaleCards)
    }

    @Test
    fun `given a tris and a scale on table when add a tris then the num of tris increase`() {

        val burracoCardsOnTable = BurracoCardsOnTable(
                listOfTris = listOf(createTris(true)),
                listOfScale = listOf(createScale(false))
        )

        val trisToAdd = createTris(false)
        val actualBurracoCardsOnTable = burracoCardsOnTable.addTris(trisToAdd)

        val expectedListOfTris = 2
        val expectedListOScale = 1

        assert(actualBurracoCardsOnTable.showScale().size == expectedListOScale)
        assert(actualBurracoCardsOnTable.showTris().size == expectedListOfTris)

    }

    @Test
    fun `given a tris and a scale on table when add a scale then the num of scale increase`() {

        val burracoCardsOnTable = BurracoCardsOnTable(
                listOfTris = listOf(createTris(true)),
                listOfScale = listOf(createScale(false))
        )

        val scaleToAdd = createScale(false)
        val actualBurracoCardsOnTable = burracoCardsOnTable.addScale(scaleToAdd)

        val expectedListOfTris = 1
        val expectedListOScale = 2

        assert(actualBurracoCardsOnTable.showScale().size == expectedListOScale)
        assert(actualBurracoCardsOnTable.showTris().size == expectedListOfTris)

    }

    @Test
    fun `given a tris Id on table when append a new card then the size of the tris increase`() {

        val trisOnTable = createTris(true)

        val burracoCardsOnTable = BurracoCardsOnTable(
                listOfTris = listOf(trisOnTable, createTris(true)),
                listOfScale = listOf()
        )
        val cardsToAppend = listOf(Card(suit = Suits.Pike, rank = Ranks.Three))

        val actualBurracoCardsOnTable = burracoCardsOnTable.appendCardOnBurraco(trisOnTable.identity(), cardsToAppend)

        val expectedNumCardsTrisOnTable = trisOnTable.numCards() + 1

        val actualNumCardsTrisOnTable = actualBurracoCardsOnTable.showTris().find { t -> t.identity() == trisOnTable.identity() }!!.showCards().size

        assert(actualNumCardsTrisOnTable == expectedNumCardsTrisOnTable)

    }

    @Test
    fun `given a scale Id on table when append a new card then, the size of the scale increase`() {

        val scaleOnTable = createScale(true)

        val burracoCardsOnTable = BurracoCardsOnTable(
                listOfTris = listOf(),
                listOfScale = listOf(scaleOnTable, createScale(true))
        )
        val cardsToAppend = listOf(Card(suit = Suits.Heart, rank = Ranks.Nine))

        val actualBurracoCardsOnTable = burracoCardsOnTable.appendCardOnBurraco(scaleOnTable.identity(), cardsToAppend)

        val expectedNumCardsScaleOnTable = scaleOnTable.numCards() + 1

        val actualNumCardsScaleOnTable = actualBurracoCardsOnTable.showScale().find { t -> t.identity() == scaleOnTable.identity() }!!.showCards().size

        assert(actualNumCardsScaleOnTable == expectedNumCardsScaleOnTable)

    }

    @Test
    fun `given a scale Id not available, when append a new card then, I receive an error`() {

        val scaleOnTable = createScale(true)

        val burracoCardsOnTable = BurracoCardsOnTable(
                listOfTris = listOf(),
                listOfScale = listOf(createScale(true))
        )
        val cardsToAppend = listOf(Card(suit = Suits.Heart, rank = Ranks.Nine))

        assertFailsWith(NoSuchElementException::class) {
            burracoCardsOnTable.appendCardOnBurraco(scaleOnTable.identity(), cardsToAppend)
        }

    }


    private fun createScale(isBurraco: Boolean): BurracoScale =
            BurracoScale.create(
                    cards = when (isBurraco) {
                        true -> ListCardsBuilder.allRanksCards().plus(ListCardsBuilder.allRanksCards()).filter { c -> c.suit == Suits.Heart }.sorted().take(7)
                        false -> ListCardsBuilder.allRanksCards().plus(ListCardsBuilder.allRanksCards()).filter { c -> c.suit == Suits.Heart }.sorted().take(6)
                    }
            )


    private fun createTris(isBurraco: Boolean): BurracoTris =
            BurracoTris.create(
                    cards = when (isBurraco) {
                        true -> ListCardsBuilder.allRanksCards().plus(ListCardsBuilder.allRanksCards()).filter { c -> c.rank == Ranks.Three }.take(7)
                        false -> ListCardsBuilder.allRanksCards().plus(ListCardsBuilder.allRanksCards()).filter { c -> c.rank == Ranks.Queen }.take(6)
                    }
            )


}