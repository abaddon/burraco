package com.abaddon83.libs.ddd

trait AssertionConcern {

  protected def assertArgumentEquals(anObject1: Any, anObject2: Any, aMessage: String): Unit = {
    if (!(anObject1 == anObject2)) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentFalse(aBoolean: Boolean, aMessage: String): Unit = {
    if (aBoolean) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentLength(aString: String, aMaximum: Int, aMessage: String): Unit = {
    val length = aString.trim.length
    if (length > aMaximum) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentLength(aString: String, aMinimum: Int, aMaximum: Int, aMessage: String): Unit = {
    val length = aString.trim.length
    if (length < aMinimum || length > aMaximum) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentNotEmpty(aString: String, aMessage: String): Unit = {
    if (aString == null || aString.trim.isEmpty) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentNotEquals(anObject1: Any, anObject2: Any, aMessage: String): Unit = {
    if (anObject1 == anObject2) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentNotEmpty(anObject: Option[Any], aMessage: String): Unit = {
    anObject match {
      case Some(value) => value
      case None => throw new IllegalArgumentException(aMessage)
    }
  }

  protected def assertArgumentRange(aValue: Double, aMinimum: Double, aMaximum: Double, aMessage: String): Unit = {
    if (aValue < aMinimum || aValue > aMaximum) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentRange(aValue: Float, aMinimum: Float, aMaximum: Float, aMessage: String): Unit = {
    if (aValue < aMinimum || aValue > aMaximum) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentRange(aValue: Int, aMinimum: Int, aMaximum: Int, aMessage: String): Unit = {
    if (aValue < aMinimum || aValue > aMaximum) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentRange(aValue: Long, aMinimum: Long, aMaximum: Long, aMessage: String): Unit = {
    if (aValue < aMinimum || aValue > aMaximum) throw new IllegalArgumentException(aMessage)
  }

  protected def assertArgumentTrue(aBoolean: Boolean, aMessage: String): Unit = {
    if (!aBoolean) throw new IllegalArgumentException(aMessage)
  }

  protected def assertStateFalse(aBoolean: Boolean, aMessage: String): Unit = {
    if (aBoolean) throw new IllegalStateException(aMessage)
  }

  protected def assertStateTrue(aBoolean: Boolean, aMessage: String): Unit = {
    if (!aBoolean) throw new IllegalStateException(aMessage)
  }

/*
  protected def assertBusinessRulesTrue(aBoolean: Boolean, aMessage: String): Unit = {
    if (!aBoolean) throw new BusinessRulesExeption(aMessage)
  }

  protected def assertBusinessRulesEquals(anObject1: Any, anObject2: Any, aMessage: String): Unit = {
    if (!(anObject1 == anObject2)) throw new BusinessRulesExeption(aMessage)
  }
*/

}
