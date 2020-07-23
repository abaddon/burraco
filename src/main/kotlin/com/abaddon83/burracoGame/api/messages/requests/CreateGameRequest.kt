package com.abaddon83.burracoGame.api.messages.requests

data class CreateGameRequest(val gameType: GameType) {

}

enum class GameType{ BURRACO }