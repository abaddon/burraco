package com.abaddon83.libs.ddd

trait AggregateIdentity {
  protected type IdType
  protected val id: IdType
  def convertTo(): IdType
  override def toString(): String = s"${getClass.getSimpleName}-${id.toString}"
}
