package com.abaddon83.burraco.mocks

import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.BurracoGame
import com.abaddon83.burraco.`match`.games.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.burraco.`match`.games.ports.GameRepositoryPort
import com.abaddon83.burraco.shares.games.{Game, GameIdentity}

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
    override def save(burracoGame: BurracoGameInitiatedTurnExecution): BurracoGameInitiatedTurnExecution = {
      BurracoGameDB.persist(burracoGame) match {
        case game: BurracoGameInitiatedTurnExecution =>  game
        case _ => throw new NoSuchElementException()
      }
    }

    override def save(burracoGame: BurracoGameInitiatedTurnEnd): BurracoGameInitiatedTurnEnd = {
      BurracoGameDB.persist(burracoGame) match {
        case game: BurracoGameInitiatedTurnEnd =>  game
        case _ => throw new NoSuchElementException()
      }
    }

    override def save(burracoGame: BurracoGameInitiated): BurracoGameInitiated = {

      BurracoGameDB.persist(burracoGame) match {
        case game: BurracoGameInitiated =>  game
        case _ => throw new NoSuchElementException()
      }
    }



  override def exists(gameIdentity: GameIdentity): Boolean = {
    BurracoGameDB.search().exists(game => game.identity() == gameIdentity)
  }

    override def findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): Future[BurracoGameWaitingPlayers] = {
      Future{
        BurracoGameDB.search().find(game => game.identity() == gameIdentity) match {
          case Some(value: BurracoGameWaitingPlayers) => value
          case Some(_) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoGameInitialisedBy(gameIdentity: GameIdentity): Future[BurracoGameInitiated] = {
      Future {
        BurracoGameDB.search().find(game => game.identity() == gameIdentity) match {
          case Some(game: BurracoGameInitiatedTurnStart) => game
          case Some(game: BurracoGameInitiatedTurnEnd) => game
          case Some(game: BurracoGameInitiatedTurnExecution) => game
          case Some(_) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoGameInitialisedTurnStartBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnStart] = {
      Future{
        BurracoGameDB.search().find(game => game.identity() == gameIdentity) match {
          case Some(value: BurracoGameInitiatedTurnStart) => value
          case Some(_) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoGameInitialisedTurnExecutionBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnExecution] = {
      Future{
        BurracoGameDB.search().find(game => game.identity() == gameIdentity) match {
          case Some(value: BurracoGameInitiatedTurnExecution) => value
          case Some(_) => throw new NoSuchElementException()
          case None => throw new NoSuchElementException()
        }
      }
    }

    override def findBurracoGameInitialisedTurnEndBy(gameIdentity: GameIdentity): Future[BurracoGameInitiatedTurnEnd] = {
      Future{
        BurracoGameDB.search().find(game => game.identity() == gameIdentity) match {
          case Some(value: BurracoGameInitiatedTurnEnd) => value
          case Some(_) => throw new NoSuchElementException()
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
    burracoGame
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