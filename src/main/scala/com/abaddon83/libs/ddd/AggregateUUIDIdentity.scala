package com.abaddon83.libs.ddd

import java.util.UUID

trait AggregateUUIDIdentity extends AggregateIdentity {
  override type IdType = UUID

  //override def toString(): String = s"${getClass.getName}-${id.toString}"
  //override def toString(): String = s"AggregateUUIDIdentity-${id.toString}"
  override def convertTo(): UUID = id.asInstanceOf[UUID]
}

object AggregateUUIDIdentity {
  def isValidUUIDString(value: String): Boolean = {
    try{
      UUID.fromString(value)
      true
    } catch {
      case e => false
    }
  }
}



