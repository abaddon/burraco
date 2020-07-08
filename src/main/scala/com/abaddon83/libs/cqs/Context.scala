package com.abaddon83.libs.cqs

import com.google.inject.Injector

import scala.reflect.ClassTag

class Context(protected val injector: Injector){

  import scala.reflect.runtime.universe._
  def func2[T](o: T)(implicit tag: TypeTag[T]): Unit = {
    tag.tpe match {
      case TypeRef(utype, usymbol, args) => println(args.toString)
      case _ => println(None)
    }
  }

  def resolve[T](implicit ct: TypeTag[T]):T ={
    injector.getInstance(ct.getClass).asInstanceOf[T]
      //.getInstance(typeLiteral[T].toKey)
  }

  def getInjector: Injector = injector

}
