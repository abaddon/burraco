package com.abaddon83.cardsGames.burracoGames.iocs

import com.abaddon83.cardsGames.burracoGames.commands.{AddPlayerCmd, AddPlayerHandler, AppendCardOnBurracoCmd, AppendCardOnBurracoHandler, CreateNewBurracoGameCmd, CreateNewBurracoGameHandler, DropCardOnDiscardPileCmd, DropCardOnDiscardPileHandler, DropScaleCmd, DropScaleHandler, DropTrisCmd, DropTrisHandler, EndGameCmd, EndGameHandler, EndPlayerTurnCmd, EndPlayerTurnHandler, OrganisePlayerCardsCmd, OrganisePlayerCardsHandler, PickUpACardFromDeckCmd, PickUpACardFromDeckHandler, PickUpCardsFromDiscardPileCmd, PickUpCardsFromDiscardPileHandler, PickUpMazzettoDeckCmd, PickUpMazzettoDeckHandler, StartGameCmd, StartGameHandler}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.ended.BurracoGameEnded
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.burracoGames.queries.{FindBurracoGameEndedHandler, FindBurracoGameEndedQuery, FindBurracoGameInitiatedHandler, FindBurracoGameInitiatedQuery, FindBurracoGameInitiatedTurnEndHandler, FindBurracoGameInitiatedTurnEndQuery, FindBurracoGameInitiatedTurnExecHandler, FindBurracoGameInitiatedTurnExecQuery, FindBurracoGameStartedTurnStartHandler, FindBurracoGameStartedTurnStartQuery, FindBurracoGameWaitingHandler, FindBurracoGameWaitingQuery}
import com.abaddon83.libs.cqs.commands.CommandHandler
import com.abaddon83.libs.cqs.queries.QueryHandler
import com.google.common.reflect.ClassPath
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class QueryModule(repositoryPort: GameRepositoryPort) extends AbstractModule with ScalaModule {
  implicit val ec: scala.concurrent.ExecutionContext= scala.concurrent.ExecutionContext.global


  override def configure(): Unit = {

    bind[QueryHandler[FindBurracoGameEndedQuery, BurracoGameEnded]].toInstance(new FindBurracoGameEndedHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameInitiatedQuery, BurracoGameInitiated]].toInstance(new FindBurracoGameInitiatedHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameInitiatedTurnEndQuery, BurracoGameInitiatedTurnEnd]].toInstance(new FindBurracoGameInitiatedTurnEndHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameInitiatedTurnExecQuery, BurracoGameInitiatedTurnExecution]].toInstance(new FindBurracoGameInitiatedTurnExecHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameStartedTurnStartQuery, BurracoGameInitiatedTurnStart]].toInstance(new FindBurracoGameStartedTurnStartHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers]].toInstance(new FindBurracoGameWaitingHandler(repositoryPort))

//    ClassPath.from(getClass.getClassLoader()).getTopLevelClasses("com.abaddon83.cardsGames.burracoGames.queries").forEach { classInfo =>
//
//      val clsOpt = Reflect.lookupInstantiatableClass(classInfo.getName, getClass.getClassLoader())
//      val cls = clsOpt.get // or any safer way to extract the Option
//      val ctor = cls.getConstructor(classOf[GameRepositoryPort]).get
//      val instance = ctor.newInstance(repositoryPort).asInstanceOf[]
//      bind(classInfo.load()).toInstance(instance);
//
//   instance   }

  }
}

class CommandModule(repositoryPort: GameRepositoryPort) extends AbstractModule with ScalaModule {
  implicit val ec: scala.concurrent.ExecutionContext= scala.concurrent.ExecutionContext.global


  override def configure(): Unit = {
    bind[CommandHandler[AddPlayerCmd]].toInstance(new AddPlayerHandler(repositoryPort))
    bind[CommandHandler[AppendCardOnBurracoCmd]].toInstance(new AppendCardOnBurracoHandler(repositoryPort))
    bind[CommandHandler[CreateNewBurracoGameCmd]].toInstance(new CreateNewBurracoGameHandler(repositoryPort))
    bind[CommandHandler[DropCardOnDiscardPileCmd]].toInstance(new DropCardOnDiscardPileHandler(repositoryPort))
    bind[CommandHandler[DropScaleCmd]].toInstance(new DropScaleHandler(repositoryPort))
    bind[CommandHandler[DropTrisCmd]].toInstance(new DropTrisHandler(repositoryPort))
    bind[CommandHandler[EndGameCmd]].toInstance(new EndGameHandler(repositoryPort))
    bind[CommandHandler[EndPlayerTurnCmd]].toInstance(new EndPlayerTurnHandler(repositoryPort))
    bind[CommandHandler[OrganisePlayerCardsCmd]].toInstance(new OrganisePlayerCardsHandler(repositoryPort))
    bind[CommandHandler[PickUpACardFromDeckCmd]].toInstance(new PickUpACardFromDeckHandler(repositoryPort))
    bind[CommandHandler[PickUpCardsFromDiscardPileCmd]].toInstance(new PickUpCardsFromDiscardPileHandler(repositoryPort))
    bind[CommandHandler[PickUpMazzettoDeckCmd]].toInstance(new PickUpMazzettoDeckHandler(repositoryPort))
    bind[CommandHandler[StartGameCmd]].toInstance(new StartGameHandler(repositoryPort))


        ClassPath.from(getClass.getClassLoader()).getTopLevelClasses("com.abaddon83.cardsGames.burracoGames.queries").forEach { classInfo =>
    //      val instance = classInfo.load().getDeclaredConstructor(classOf[GameRepositoryPort]).newInstance(repositoryPort)
    //        instance.asInstanceOf[QueryHandler[FindBurracoGameEndedQuery, BurracoGameEnded]]
    //        bind(classInfo.load()).toInstance(instance);
          }

  }
}