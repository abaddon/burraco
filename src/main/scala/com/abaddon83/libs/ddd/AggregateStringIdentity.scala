package com.abaddon83.libs.ddd

trait AggregateStringIdentity extends AggregateIdentity {
  override type IdType = String

  //override def toString(): String = s"AggregateStringIdentity-${id}"
  override def convertTo(): String = id.asInstanceOf[String]
}
