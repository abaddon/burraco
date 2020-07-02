package com.abaddon83.cardsGames.burracoGames.iocs

import com.abaddon83.cardsGames.burracoGames.ports.GameRepositoryPort
import com.abaddon83.cardsGames.burracoGames.queries.FindBurracoGameEndedHandler
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class CQSModule(repositoryPort: GameRepositoryPort) extends AbstractModule with ScalaModule {

  import com.google.common.reflect.ClassPath

  override def configure(): Unit = {
    ClassPath.from(getClass.getClassLoader()).getTopLevelClasses("com.abaddon83.cardsGames.burracoGames.queries")
      .forEach { classInfo =>

        if (classInfo.getSimpleName == "FindBurracoGameEndedHandler") {
          println(classInfo.getSimpleName)

          bind(classInfo.load()).asEagerSingleton()
        }
        //val constructor = classInfo.load().getConstructor()
        //val args = Array[AnyRef](repositoryPort)
        //val instance = constructor.newInstance(args:_*).asInstanceOf[classInfo.type ]

      }

  }
}