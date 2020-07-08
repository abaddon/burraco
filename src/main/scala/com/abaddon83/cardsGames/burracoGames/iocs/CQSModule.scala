package com.abaddon83.cardsGames.burracoGames.iocs

import com.abaddon83.cardsGames.burracoGames.commands.{AddPlayerCmd, AddPlayerHandler, AppendCardOnBurracoCmd, AppendCardOnBurracoHandler, CreateNewBurracoGameCmd, CreateNewBurracoGameHandler, DropCardOnDiscardPileCmd, DropCardOnDiscardPileHandler, DropScaleCmd, DropScaleHandler, DropTrisCmd, DropTrisHandler, EndGameCmd, EndGameHandler, EndPlayerTurnCmd, EndPlayerTurnHandler, OrganisePlayerCardsCmd, OrganisePlayerCardsHandler, PickUpACardFromDeckCmd, PickUpACardFromDeckHandler, PickUpCardsFromDiscardPileCmd, PickUpCardsFromDiscardPileHandler, PickUpMazzettoDeckCmd, PickUpMazzettoDeckHandler, StartGameCmd, StartGameHandler}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.BurracoGame
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.ended.BurracoGameEnded
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.initialised.{BurracoGameInitiated, BurracoGameInitiatedTurnEnd, BurracoGameInitiatedTurnExecution, BurracoGameInitiatedTurnStart}
import com.abaddon83.cardsGames.burracoGames.domainModels.burracoGames.waitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.cardsGames.burracoGames.ports.BurracoGameRepositoryPort
import com.abaddon83.cardsGames.burracoGames.queries.{FindBurracoGameEndedHandler, FindBurracoGameEndedQuery, FindBurracoGameHandler, FindBurracoGameInitiatedHandler, FindBurracoGameInitiatedQuery, FindBurracoGameInitiatedTurnEndHandler, FindBurracoGameInitiatedTurnEndQuery, FindBurracoGameInitiatedTurnExecHandler, FindBurracoGameInitiatedTurnExecQuery, FindBurracoGameQuery, FindBurracoGameStartedTurnStartHandler, FindBurracoGameStartedTurnStartQuery, FindBurracoGameWaitingHandler, FindBurracoGameWaitingQuery}
import com.abaddon83.libs.cqs.commands.CommandHandler
import com.abaddon83.libs.cqs.queries.QueryHandler
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Injector}
import net.codingwell.scalaguice.ScalaModule

class QueryModule(injector: Injector)(implicit val ec: scala.concurrent.ExecutionContext) extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    import net.codingwell.scalaguice.InjectorExtensions._
    val repositoryPort = injector.instance[BurracoGameRepositoryPort]
    bind[QueryHandler[FindBurracoGameEndedQuery, BurracoGameEnded]].toInstance(new FindBurracoGameEndedHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameInitiatedQuery, BurracoGameInitiated]].toInstance(new FindBurracoGameInitiatedHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameInitiatedTurnEndQuery, BurracoGameInitiatedTurnEnd]].toInstance(new FindBurracoGameInitiatedTurnEndHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameInitiatedTurnExecQuery, BurracoGameInitiatedTurnExecution]].toInstance(new FindBurracoGameInitiatedTurnExecHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameStartedTurnStartQuery, BurracoGameInitiatedTurnStart]].toInstance(new FindBurracoGameStartedTurnStartHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameWaitingQuery, BurracoGameWaitingPlayers]].toInstance(new FindBurracoGameWaitingHandler(repositoryPort))
    bind[QueryHandler[FindBurracoGameQuery, BurracoGame]].toInstance(new FindBurracoGameHandler(repositoryPort))


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

class CommandModule(injector: Injector)(implicit val ec: scala.concurrent.ExecutionContext) extends AbstractModule with ScalaModule {

  import net.codingwell.scalaguice.InjectorExtensions._

  override def configure(): Unit = {
    val repositoryPort = injector.instance[BurracoGameRepositoryPort]


    bind[CommandHandler[AddPlayerCmd]].annotatedWith(Names.named("AddPlayerCmd")).toInstance(new AddPlayerHandler(repositoryPort))
    bind[CommandHandler[AppendCardOnBurracoCmd]].annotatedWith(Names.named("AppendCardOnBurracoCmd")).toInstance(new AppendCardOnBurracoHandler(repositoryPort))
    bind[CommandHandler[CreateNewBurracoGameCmd]].annotatedWith(Names.named("CreateNewBurracoGameCmd")).toInstance(new CreateNewBurracoGameHandler(repositoryPort))
    bind[CommandHandler[DropCardOnDiscardPileCmd]].annotatedWith(Names.named("DropCardOnDiscardPileCmd")).toInstance(new DropCardOnDiscardPileHandler(repositoryPort))
    bind[CommandHandler[DropScaleCmd]].annotatedWith(Names.named("DropScaleCmd")).toInstance(new DropScaleHandler(repositoryPort))
    bind[CommandHandler[DropTrisCmd]].annotatedWith(Names.named("DropTrisCmd")).toInstance(new DropTrisHandler(repositoryPort))
    bind[CommandHandler[EndGameCmd]].annotatedWith(Names.named("EndGameCmd")).toInstance(new EndGameHandler(repositoryPort))
    bind[CommandHandler[EndPlayerTurnCmd]].annotatedWith(Names.named("EndPlayerTurnCmd")).toInstance(new EndPlayerTurnHandler(repositoryPort))
    bind[CommandHandler[OrganisePlayerCardsCmd]].annotatedWith(Names.named("OrganisePlayerCardsCmd")).toInstance(new OrganisePlayerCardsHandler(repositoryPort))
    bind[CommandHandler[PickUpACardFromDeckCmd]].annotatedWith(Names.named("PickUpACardFromDeckCmd")).toInstance(new PickUpACardFromDeckHandler(repositoryPort))
    bind[CommandHandler[PickUpCardsFromDiscardPileCmd]].annotatedWith(Names.named("PickUpCardsFromDiscardPileCmd")).toInstance(new PickUpCardsFromDiscardPileHandler(repositoryPort))
    bind[CommandHandler[PickUpMazzettoDeckCmd]].annotatedWith(Names.named("PickUpMazzettoDeckCmd")).toInstance(new PickUpMazzettoDeckHandler(repositoryPort))
    bind[CommandHandler[StartGameCmd]].annotatedWith(Names.named("StartGameCmd")).toInstance(new StartGameHandler(repositoryPort))


    //    ClassPath.from(getClass.getClassLoader()).getTopLevelClasses("com.abaddon83.cardsGames.burracoGames.queries").forEach { classInfo =>
    //      val instance = classInfo.load().getDeclaredConstructor(classOf[GameRepositoryPort]).newInstance(repositoryPort)
    //        instance.asInstanceOf[QueryHandler[FindBurracoGameEndedQuery, BurracoGameEnded]]
    //        bind(classInfo.load()).toInstance(instance);
  }

}