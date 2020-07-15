package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.messages.requests

data class CreateGameRequest(val gameType: GameType) {

}

enum class GameType{ BURRACO }