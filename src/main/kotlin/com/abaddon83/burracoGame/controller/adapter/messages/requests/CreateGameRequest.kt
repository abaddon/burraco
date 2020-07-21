package com.abaddon83.burracoGame.controller.adapter.messages.requests

data class CreateGameRequest(val gameType: GameType) {

}

enum class GameType{ BURRACO }