package com.abaddon83.utils

import java.util.UUID
import scala.collection.mutable.ListBuffer

case object UUIDRegistryHelper {
  private var list: ListBuffer[Element] = new ListBuffer[Element]()

  def add(itemType: String, uuid: UUID, status: String): Unit ={
    if(exist(itemType,uuid)){
      throw new Exception("UUIDRepositoryHelper element collision")
    }
    list.addOne(Element(itemType,uuid,status))
  }

  def update(itemType: String, uuid: UUID, status: String): Unit ={
    val element  = find(itemType, uuid).get
    remove(element)
    add(itemType,uuid,status)
  }

  def search(itemType: String,status: String):Option[UUID] = {
    list.find(e => e.itemType==itemType && e.status == status).map(e => e.uuid)
  }

  def printAll() = {
    println("printAll() started")
    list.foreach(element =>
      println(element)
    )
    println("printAll() ended")
  }

  private def remove(element: Element): Unit ={
    list -= element
  }


  private def exist(itemType: String, uuid: UUID): Boolean ={
    find(itemType, uuid) match {
      case Some(value) =>true
      case None =>false
    }
  }

  private def find(itemType: String, uuid: UUID): Option[Element] = {
    list.find(e => e.itemType == itemType && e.uuid == uuid)
  }
}

case class Element( itemType: String,
                    uuid: UUID,
                    status: String
                    ) {
  override def toString: String = {
    s"itemType: ${itemType}, uuid: ${uuid}, status: ${status}"
  }
}



