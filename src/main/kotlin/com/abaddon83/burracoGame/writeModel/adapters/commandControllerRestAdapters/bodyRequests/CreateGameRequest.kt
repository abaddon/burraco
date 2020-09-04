package com.abaddon83.burracoGame.writeModel.adapters.commandControllerRestAdapters.bodyRequests

data class CreateGameRequest(val gameType: GameType) {

}

enum class GameType{ BURRACO }