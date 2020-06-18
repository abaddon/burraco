package com.abaddon83.burraco.mocks

trait MockExecutionContext {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

}
