package com.abaddon83.libs.cqs

import com.google.inject.Injector

import scala.reflect.ClassTag

class Context(protected val injector: Injector){


  def resolve[T](implicit ct: ClassTag[T]):T ={
    injector.getInstance(ct.runtimeClass).asInstanceOf[T]
      //.getInstance(typeLiteral[T].toKey)
  }

  def getInjector: Injector = injector

}
