package com.abaddon83.burracoGame.api.routes

//fun Routing.apiBurracoGames(commandController: CommandControllerPort, readModelController: BurracoGameReadModelControllerPort) {
//
//    route("games/burraco/{gameId}") {
//        get() {
//            val gameIdentity = requireNotNull(UUID.fromString(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
//            val burracoGame = readModelController.findBurracoGame(gameIdentity)
//                    ?: throw NoSuchElementException("BurracoGame with $gameIdentity not found")
//
//            call.respond(BurracoGameModule.from(burracoGame))
//        }
//        route("players") {
//            route("{playerId}") {
////                route("cards") {
////                    get {
////                        val gameIdentity = requireNotNull(GameIdentity.create(call.parameters["gameId"]!!)) { "PlayerIdentity ${call.parameters["gameId"] ?: "null"} missing or malformed" }
////                        val playerIdentity = requireNotNull(PlayerIdentity.create(call.parameters["playerId"]!!)) { "PlayerIdentity ${call.parameters["playerId"] ?: "null"} missing or malformed" }
////                        val playerCards = readModelController.showPlayerCards(burracoGameIdentity = gameIdentity, playerIdentity = playerIdentity)
////                        call.respond(CardsModule.from(playerCards))
////                    }
////                }
//            }
//
//        }
//    }
//}