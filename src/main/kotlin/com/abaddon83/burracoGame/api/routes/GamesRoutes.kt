package com.abaddon83.burracoGame.api.routes

//fun Routing.apiGames(commandController: CommandControllerPort, readModelController: BurracoGameReadModelControllerPort) {
//    route("games") {
//        post {
//            val request = call.receive<CreateGameRequest>()
//            val gameIdentity = GameIdentity.create()
//            when (request.gameType) {
//                GameType.BURRACO -> commandController.createNewBurracoGame(gameIdentity)
//            }
//            //TODO query to add
//            call.respond(gameIdentity)
//        }
//    }
//}

//        get("/") {
//            call.respond(characterController.allCharacterIdentities().map{it.value}.toList())
//        }
//
//        get("/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id")
//            val targetLang = call.request.queryParameters["language"]
//            val character = {
//                if(targetLang.isNullOrEmpty())
//                    characterController.character(CharacterIdentity(id))
//                else
//                    characterController.characterLocalised(CharacterIdentity(id),targetLang)
//            }.invoke()
//            call.respond(JsonCharacter(character))
//        }