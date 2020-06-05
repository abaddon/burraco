package com.abaddon83.burraco.mocks

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoGame, BurracoGameInitialised, BurracoGameWaitingPlayers}
import com.abaddon83.burraco.`match`.games.ports.BurracoGameRepositoryPort
import com.abaddon83.burraco.shares.games.GameIdentity

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

trait MockBurracoGameRepositoryAdapter {
implicit val ec: scala.concurrent.ExecutionContext

val mockBurracoGameRepositoryAdapter = new BurracoGameRepositoryPort {

    override def save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers = {
      BurracoGameDB.persist(burracoGame) match {
        case game: BurracoGameWaitingPlayers =>  game
        case _ => throw new NoSuchElementException()
      }
    }

    override def save(burracoGame: BurracoGameInitialised): BurracoGameInitialised = {
      BurracoGameDB.persist(burracoGame) match {
        case game: BurracoGameInitialised =>  game
        case _ => throw new NoSuchElementException()
      }
    }

    override def findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): Future[BurracoGameWaitingPlayers] = {
      Future{
        BurracoGameDB.search().find(game => game.gameIdentity == gameIdentity) match {
          case Some(value: BurracoGameWaitingPlayers) => value
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoGameInitialisedBy(gameIdentity: GameIdentity): Future[BurracoGameInitialised] = {
      Future{
        BurracoGameDB.search().find(game => game.gameIdentity == gameIdentity) match {
          case Some(value: BurracoGameInitialised) => value
          case None => throw new NoSuchElementException()
        }
      }
    }
  }
}

protected object BurracoGameDB{
  private val db = new ListBuffer[BurracoGame]

  def persist(burracoGame: BurracoGame): BurracoGame = {
    if(isAnUpdate(burracoGame.gameIdentity)){
      update(burracoGame)
    }else{
      add(burracoGame)
    }
    search().find(game => game.gameIdentity == burracoGame.gameIdentity).get
  }

  def search(): ListBuffer[BurracoGame] = db;

  private def add(burracoGame: BurracoGame): Unit ={
    db.addOne(burracoGame)
  }

  private def update(burracoGame: BurracoGame): Unit = {
    val gameToRemove= db.find(game => game.gameIdentity == burracoGame.gameIdentity).get
    db-=gameToRemove
    db+=burracoGame
  }

  private def isAnUpdate(gameIdentity: GameIdentity): Boolean = {
    db.exists(burracoGame => burracoGame.gameIdentity == gameIdentity)
  }
}