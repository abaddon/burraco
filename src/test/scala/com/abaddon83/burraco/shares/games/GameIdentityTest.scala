package com.abaddon83.burraco.shares.games

import java.util.UUID

import org.scalatest.funsuite.AnyFunSuite

class GameIdentityTest extends AnyFunSuite{

  test("new GameIdentity using UUID"){
    val expectedUUID = UUID.randomUUID()
    val identity = GameIdentity(expectedUUID)
    assert(identity.isInstanceOf[GameIdentity])
    assert(identity.convertTo() == expectedUUID)
  }

  test("new GameIdentity using a valid UUID String"){
    val expectedUUID = UUID.randomUUID()
    val identity = GameIdentity(expectedUUID.toString)
    assert(identity.isInstanceOf[GameIdentity])
    assert(identity.convertTo() == expectedUUID)
  }

  test("new GameIdentity using a not valid UUID String should fail"){

    assertThrows[AssertionError]{
      GameIdentity("fake-UUID")
    }
  }

}
