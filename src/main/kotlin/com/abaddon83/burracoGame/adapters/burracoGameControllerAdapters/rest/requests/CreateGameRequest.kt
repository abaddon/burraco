package com.abaddon83.burracoGame.adapters.burracoGameControllerAdapters.rest.requests

data class CreateGameRequest(val gameType: GameType) {

}

enum class GameType{ BURRACO }