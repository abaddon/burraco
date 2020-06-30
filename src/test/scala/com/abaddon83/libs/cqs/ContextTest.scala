package com.abaddon83.libs.cqs

import java.util.UUID

import com.abaddon83.cardsGames.burracoGames.commands.{CreateNewBurracoGameCmd, CreateNewBurracoGameHandler}
import com.abaddon83.cardsGames.mocks.MockBurracoGameRepositoryAdapter
import com.abaddon83.libs.cqs.commands.{Command, CommandHandler}
import com.google.inject.{AbstractModule, Guice}
import net.codingwell.scalaguice.InjectorExtensions._
import net.codingwell.scalaguice.ScalaModule
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.Future



class ContextTest extends AnyFunSuite with MockBurracoGameRepositoryAdapter{
  implicit val ec: scala.concurrent.ExecutionContext= scala.concurrent.ExecutionContext.global

  test("Text context"){
    val injector = Guice.createInjector(new TestModule())

    val context = new Context(injector)
    assert(context.getInjector.instance[CommandHandler[CreateNewBurracoGameCmd]].isInstanceOf[CreateNewBurracoGameHandler])
    assert(context.getInjector.instance[CommandHandler[UpdateGame]].isInstanceOf[UpdateGameHandler])
  }

}

case class UpdateGame() extends Command {
  override protected val requestId: UUID = UUID.randomUUID()
}

case class UpdateGameHandler()(implicit val ec: scala.concurrent.ExecutionContext) extends CommandHandler[UpdateGame]{

  override def handleAsync(command: UpdateGame): Future[Unit] = {
    Future{
???
    }
  }

  override def handle(command: UpdateGame): Unit = ???
}

class TestModule extends AbstractModule with ScalaModule with MockBurracoGameRepositoryAdapter {
  implicit val ec: scala.concurrent.ExecutionContext= scala.concurrent.ExecutionContext.global
  override def configure(): Unit = {

    bind[CommandHandler[CreateNewBurracoGameCmd]].toInstance(new CreateNewBurracoGameHandler(mockBurracoGameRepositoryAdapter))
    bind[CommandHandler[UpdateGame]].toInstance(UpdateGameHandler())


    //bind[CreditCardPaymentService]
    //bind[Bar[Foo]].to[FooBarImpl]
    //bind[PaymentService].annotatedWith(Names.named("paypal")).to[CreditCardPaymentService]
  }
}
