package com.abaddon83.libs.cqs.commands

import java.util.UUID

import com.abaddon83.libs.cqs.Request

trait Command extends Request {
  protected val requestId: UUID

}
