package com.abaddon83.burracoGame.commandModel.adapters.BurracoGameCommandControllerAdapters

import com.abaddon83.burracoGame.commandModel.commands.*
import com.abaddon83.burracoGame.commandModel.models.decks.Card
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.burracoGame.commandModel.models.players.PlayerIdentity
import com.abaddon83.burracoGame.commandModel.ports.BurracoGameCommandControllerPort

class BurracoGameCommandControllerAdapter: BurracoGameCommandControllerPort {

    override suspend fun createNewBurracoGame(gameIdentity: GameIdentity) {
        val command = CreateNewBurracoGameCmd(gameIdentity)
        service.createNewBurracoGame(command)
    }

    override suspend fun joinPlayer(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity) {
        val command = AddPlayerCmd(gameIdentity = burracoGameIdentity,playerIdentityToAdd = playerIdentity)
        service.addPlayer(command)
    }


    override suspend fun startGame(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity) {
        val command = StartGameCmd(gameIdentity = burracoGameIdentity)
        service.startGame(command)
    }

    override suspend fun pickUpCardFromDeck(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity) {
        val command = PickUpACardFromDeckCmd(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity)
        service.pickUpACardFromDeck(command)
    }

    override suspend fun dropCardOnDiscardPile(burracoGameIdentity: GameIdentity, playerIdentity: PlayerIdentity, cardToDrop: Card) {
        val command = DropCardOnDiscardPileCmd(gameIdentity = burracoGameIdentity, playerIdentity = playerIdentity,card = cardToDrop)
        service.dropCardOnDiscardPile(command)
    }
}