package com.abaddon83.cardsGames.mocks

import com.abaddon83.cardsGames.burracoGames.domainModels.{BurracoPlayer, PlayerNotAssigned}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.playerInGames.PlayerInGame
import com.abaddon83.cardsGames.burracoGames.ports.PlayerPort
import com.abaddon83.cardsGames.shares.players.PlayerIdentity
import com.abaddon83.cardsGames.testutils.WithExecutionContext

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

trait MockPlayerAdapter extends WithExecutionContext {

  val mockPlayerAdapter = new PlayerPort {
    override def findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): Future[PlayerNotAssigned] = {
      Future {
        PlayerDB.search().find(player => player.playerIdentity == playerIdentity) match {
          case Some(value: PlayerNotAssigned) => value
          case Some(value) => throw new NoSuchElementException(s"${playerIdentity} not found in status PlayerNotAssigned")
          case None => throw new NoSuchElementException(s"${playerIdentity} not found")
        }
      }
    }

    override def findBurracoPlayerBy(playerIdentity: PlayerIdentity): Future[BurracoPlayer] = {
      Future {
        PlayerDB.search().find(player => player.playerIdentity == playerIdentity) match {
          case Some(value: BurracoPlayer) => value
          case Some(value) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): Future[PlayerInGame] = {
      Future {
        PlayerDB.search().find(player => player.playerIdentity == playerIdentity) match {
          case Some(value: PlayerInGame) => value
          case Some(value) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    def updatePlayerStatusToPlayerAssigned(playerIdentity: PlayerIdentity): Unit = {
      val idx = PlayerDB.search().indexWhere(p => p.playerIdentity== playerIdentity)
      assert(idx != -1)
      PlayerDB.search().remove(idx)
      PlayerDB.search().addOne(PlayerNotAssigned(playerIdentity).asInstanceOf[BurracoPlayer])
    }

    def mockPlayer():ListBuffer[BurracoPlayer] = {
      PlayerDB.search()
    }
  }
}

protected object PlayerDB{
  private val db: ListBuffer[BurracoPlayer] = ListBuffer(
    PlayerNotAssigned(PlayerIdentity("75673281-5c5b-426e-898f-b8ebbef532ee")).asInstanceOf[BurracoPlayer],
    PlayerNotAssigned(PlayerIdentity("1e515b66-a51d-43b9-9afe-c847911ff739")).asInstanceOf[BurracoPlayer],
    PlayerNotAssigned(PlayerIdentity("a11de97d-d46e-4d73-9f74-a9b0bf7665a5")).asInstanceOf[BurracoPlayer]
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