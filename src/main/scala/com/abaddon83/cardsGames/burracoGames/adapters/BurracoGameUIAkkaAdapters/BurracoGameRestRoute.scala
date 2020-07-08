package com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.requests.CreateGameRequest
import com.abaddon83.cardsGames.burracoGames.adapters.BurracoGameUIAkkaAdapters.responses.BurracoGameResponse
import com.abaddon83.cardsGames.burracoGames.services.BurracoGameService
import spray.json._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class BurracoGameRestRoute(burracoGameService: BurracoGameService)(implicit actorSystem: ActorSystem)
  extends Directives with BurracoGameJsonSupport {

  val route: Route = {
    extractUri { uri =>
      pathPrefix("games") {
        pathEndOrSingleSlash {
          post {
            entity(as[CreateGameRequest]) { request =>
              val game: Future[BurracoGameWaitingPlayers] = burracoGameService.createNewBurracoGame()
              onComplete(game) {
                _ match {
                  case Failure(exception) => complete(exception.getMessage) //complete(StatusCodes.InternalServerError,ErrorMessage.apply(exception, uri.path.toString()))
                  case Success(game) => {
                    complete(BurracoGameResponse(identity = game.gameIdentity.convertTo(), players = game.numPlayers))
                  }
                }
                              }
            }
          }
        }
      }
    }

  }
}


