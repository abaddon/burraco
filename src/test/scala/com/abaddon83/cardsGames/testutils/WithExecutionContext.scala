package com.abaddon83.cardsGames.testutils

trait WithExecutionContext {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

}
