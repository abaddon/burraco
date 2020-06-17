package com.abaddon83.burraco.mocks

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.BurracoGameInitiatedTurnStart
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.GameIdentity

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

trait MockBurracoGameRepositoryAdapter {
implicit val ec: scala.concurrent.ExecutionContext

val mockBurracoGameRepositoryAdapter = new GameRepositoryPort {

    override def save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers = {
      BurracoGameDB.persist(burracoGame) match {
        case game: BurracoGameWaitingPlayers =>  game
        case _ => throw new NoSuchElementException()
      }
    }

    override def save(burracoGame: BurracoGameInitiatedTurnStart): BurracoGameInitiatedTurnStart = {
      BurracoGameDB.persist(burracoGame) match {
        case game: BurracoGameInitiatedTurnStart =>  game
        case _ => throw new NoSuchElementException()
      }
    }

    override def findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): Future[BurracoGameWaitingPlayers] = {
      Future{
        BurracoGameDB.search().find(game => game.identity() == gameIdentity) match {
          case Some(value: BurracoGameWaitingPlayers) => value
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoGameInitialisedBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnStart] = {
      Future{
        BurracoGameDB.search().find(game => game.identity() == gameIdentity) match {
          case Some(value: BurracoGameInitiatedTurnStart) => value
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findAllBurracoGameWaitingPlayers(): Future[List[BurracoGameWaitingPlayers]] = {
      Future{
        BurracoGameDB.search().filter(game =>
          game.isInstanceOf[BurracoGameWaitingPlayers]
        ).map( game =>
          game.asInstanceOf[BurracoGameWaitingPlayers]
        ).toList
      }
    }

  }
}

protected object BurracoGameDB{
  private val db = new ListBuffer[BurracoGame]

  def persist(burracoGame: BurracoGame): BurracoGame = {
    if(isAnUpdate(burracoGame.identity())){
      update(burracoGame)
    }else{
      add(burracoGame)
    }
    search().find(game => game.identity() == burracoGame.identity()).get
  }

  def search(): ListBuffer[BurracoGame] = db;

  private def add(burracoGame: BurracoGame): Unit ={
    db.addOne(burracoGame)
  }

  private def update(burracoGame: BurracoGame): Unit = {
    val gameToRemove= db.find(game => game.identity() == burracoGame.identity()).get
    db-=gameToRemove
    db+=burracoGame
  }

  private def isAnUpdate(gameIdentity: GameIdentity): Boolean = {
    db.exists(burracoGame => burracoGame.identity() == gameIdentity)
  }
}