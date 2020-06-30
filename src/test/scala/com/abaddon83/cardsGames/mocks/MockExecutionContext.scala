package com.abaddon83.cardsGames.mocks

trait MockExecutionContext {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

}
