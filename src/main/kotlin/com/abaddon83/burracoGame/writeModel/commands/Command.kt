package com.abaddon83.burracoGame.writeModel.commands

import com.abaddon83.burracoGame.writeModel.models.BurracoScale
import com.abaddon83.burracoGame.writeModel.models.BurracoTris
import com.abaddon83.burracoGame.writeModel.models.burracos.BurracoIdentity
import com.abaddon83.burracoGame.writeModel.models.decks.Card
import com.abaddon83.burracoGame.writeModel.models.games.GameIdentity
import com.abaddon83.burracoGame.writeModel.models.players.PlayerIdentity
import com.abaddon83.utils.cqs.commands.Command
import java.sql.Timestamp
import java.util.*

sealed class CommandImpl(override val commandId: UUID = UUID.randomUUID(), override val commandTimeStamp: Timestamp = Timestamp(System.currentTimeMillis())): Command

data class CreateNewBurracoGameCmd( val gameIdentity: GameIdentity) : CommandImpl()
data class AddPlayerCmd(val gameIdentity: GameIdentity, val playerIdentityToAdd: PlayerIdentity) : CommandImpl()
data class StartGameCmd(val gameIdentity: GameIdentity): CommandImpl()
data class PickUpACardFromDeckCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity): CommandImpl()
data class PickUpCardsFromDiscardPileCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity): CommandImpl()
data class DropTrisCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity, val tris: BurracoTris): CommandImpl()
data class DropScaleCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity, val scale: BurracoScale): CommandImpl()
data class PickUpMazzettoDeckCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity ): CommandImpl()
data class AppendCardOnBurracoCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity, val burracoIdentity: BurracoIdentity, val cardsToAppend: List<Card>): CommandImpl()
data class DropCardOnDiscardPileCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity, val card: Card): CommandImpl()
data class EndPlayerTurnCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity): CommandImpl()
data class EndGameCmd(val gameIdentity: GameIdentity, val playerIdentity: PlayerIdentity): CommandImpl()

