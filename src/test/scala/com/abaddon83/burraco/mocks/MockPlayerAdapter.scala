package com.abaddon83.burraco.mocks

import com.abaddon83.burraco.`match`.games.domainModels.{BurracoPlayer, BurracoPlayerAssigned, BurracoPlayerInGame, PlayerNotAssigned}
import com.abaddon83.burraco.`match`.games.ports.PlayerPort
import com.abaddon83.burraco.shares.players.PlayerIdentity

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

trait MockPlayerAdapter {
  implicit val ec: scala.concurrent.ExecutionContext

  val mockPlayerAdapter = new PlayerPort {
    override def findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): Future[PlayerNotAssigned] = {
      Future {
        PlayerDB.search().find(player => player.playerIdentity == playerIdentity) match {
          case Some(value: PlayerNotAssigned) => value
          case Some(value) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoPlayerAssignedBy(playerIdentity: PlayerIdentity): Future[BurracoPlayerAssigned] = {
      Future {
        PlayerDB.search().find(player => player.playerIdentity == playerIdentity) match {
          case Some(value: BurracoPlayerAssigned) => value
          case Some(value) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): Future[BurracoPlayerInGame] = {
      Future {
        PlayerDB.search().find(player => player.playerIdentity == playerIdentity) match {
          case Some(value: BurracoPlayerInGame) => value
          case Some(value) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }
  }
}

protected object PlayerDB{
  private val db: ListBuffer[BurracoPlayer] = ListBuffer(
    PlayerNotAssigned(PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")).asInstanceOf[BurracoPlayer],
    PlayerNotAssigned(PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff739")).asInstanceOf[BurracoPlayer],
    BurracoPlayerAssigned(PlayerIdentity("a11de97d-d46e-4d73-9f74-a9b0bf7665a5")).asInstanceOf[BurracoPlayer]
  )

  def search(): ListBuffer[BurracoPlayer] = db;

  private def add(burracoPlayer: BurracoPlayer): Unit ={
    db.addOne(burracoPlayer)
  }

  private def update(burracoPlayer: BurracoPlayer): Unit = {
    val playerToRemove= db.find(player => player.playerIdentity == burracoPlayer.playerIdentity).get
    db-=playerToRemove
    db+=burracoPlayer
  }

  private def isAnUpdate(playerIdentity: PlayerIdentity): Boolean = {
    db.exists(burracoPlayer => burracoPlayer.playerIdentity == playerIdentity)
  }
}