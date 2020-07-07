package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters

import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.requests.CreateGameRequest
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.responses.BurracoGameResponse
import com.abaddon83.libs.akkaHttp.messages.GenericJsonSupport


trait BurracoGameJsonSupport extends GenericJsonSupport {
  //Requests
  implicit val CreateGameFormat = jsonFormat1(CreateGameRequest.apply)

  //Responses
  implicit val BurracoGameResponseFormat = jsonFormat2(BurracoGameResponse)




}
