package com.abaddon83.utils.ddd

import java.util.*
import kotlin.reflect.KClass

abstract class Identity<T>(private val id: T){
    fun convertTo(): T {
        return id
    }
    override fun toString(): String = "$${id.toString()}"
}





//data class Test(val id: UUIDIdentity){
//
//}
//
//val c = Test(UUIDIdentity(UUID.randomUUID()))

/*
trait AggregateIdentity {
  protected type IdType
  protected val id: IdType
  def convertTo(): IdType
  override def toString(): String = s"${getClass.getSimpleName}-${id.toString}"
}
 */