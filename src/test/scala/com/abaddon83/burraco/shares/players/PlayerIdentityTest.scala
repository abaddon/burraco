package com.abaddon83.burraco.shares.players

import java.util.UUID

import org.scalatest.funsuite.AnyFunSuite

class PlayerIdentityTest extends AnyFunSuite{

  test("new PlayerIdentity using UUID"){
    val expectedUUID = UUID.randomUUID()
    val identity = PlayerIdentity(expectedUUID)
    assert(identity.isInstanceOf[PlayerIdentity])
    assert(identity.convertTo() == expectedUUID)
  }

  test("new PlayerIdentity using a valid UUID String"){
    val expectedUUID = UUID.randomUUID()
    val identity = PlayerIdentity(expectedUUID.toString)
    assert(identity.isInstanceOf[PlayerIdentity])
    assert(identity.convertTo() == expectedUUID)
  }

  test("new PlayerIdentity using a not valid UUID String should fail"){

    assertThrows[AssertionError]{
      PlayerIdentity("fake-UUID")
    }
  }

}
