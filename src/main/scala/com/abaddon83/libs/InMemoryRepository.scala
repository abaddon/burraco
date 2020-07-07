package com.abaddon83.libs

import scala.collection.mutable.ListBuffer


trait InMemoryRepository[E] {
  protected object repository{
    var db: ListBuffer[E]= ListBuffer()
  }

  protected def persist(entity: E) = {
    repository.db = repository.db.addOne(entity)

    //debug()
  }

  protected def update(OldEntity: E,updatedEntity: E) = {
    repository.db = repository.db-=OldEntity
    persist(updatedEntity)
  }
}
