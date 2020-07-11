package com.abaddon83.burracoGame.adapters.playerAdapters.inMemories

import com.abaddon83.burracoGame.domainModels.BurracoPlayer
import com.abaddon83.burracoGame.domainModels.PlayerNotAssigned
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.playerInGames.PlayerInGame
import com.abaddon83.burracoGame.ports.PlayerPort
import com.abaddon83.burracoGame.shared.players.PlayerIdentity

class PlayerInMemoryAdapter: PlayerPort {
    override suspend fun findPlayerNotAssignedBy(playerIdentity: PlayerIdentity): PlayerNotAssigned =
        when(val player = PlayerDB.search().find { player -> player.identity() == playerIdentity}) {
            is PlayerNotAssigned -> player
            else -> throw NoSuchElementException("$playerIdentity not found")
        }

    override suspend fun findBurracoPlayerBy(playerIdentity: PlayerIdentity): BurracoPlayer =
        when(val player = PlayerDB.search().find{player -> player.identity() == playerIdentity}) {
            is BurracoPlayer -> player
            else -> throw NoSuchElementException()
        }

    override suspend fun findBurracoPlayerInGameBy(playerIdentity: PlayerIdentity): PlayerInGame =
        when(val player = PlayerDB.search().find { player -> player.identity() == playerIdentity }) {
            is PlayerInGame -> player
            else -> throw NoSuchElementException()
        }

    fun updatePlayerStatusToPlayerAssigned(playerIdentity: PlayerIdentity): Unit  {
        val idx = PlayerDB.search().indexOfFirst { player -> player.identity() == playerIdentity }
        assert(idx != -1)
        PlayerDB.search().removeAt(idx)
        PlayerDB.search().add(PlayerNotAssigned(playerIdentity) as BurracoPlayer)
    }

}

object PlayerDB{
    private val db: MutableList<BurracoPlayer> = mutableListOf(
    PlayerNotAssigned(PlayerIdentity.create("75673281-5c5b-426e-898f-b8ebbef532ee")) as BurracoPlayer,
    PlayerNotAssigned(PlayerIdentity.create("1e515b66-a51d-43b9-9afe-c847911ff739")) as BurracoPlayer,
    PlayerNotAssigned(PlayerIdentity.create("a11de97d-d46e-4d73-9f74-a9b0bf7665a5")) as BurracoPlayer
    )

    fun search(): MutableList<BurracoPlayer> = db;

    private fun add(burracoPlayer: BurracoPlayer): Unit {
        db.add(burracoPlayer)
    }

    private fun update(burracoPlayer: BurracoPlayer): Unit {
        val playerToRemove = checkNotNull(db.find { player -> player.identity() == burracoPlayer.identity()})
        db.remove(playerToRemove)
        db.add(burracoPlayer)
    }

    private fun isAnUpdate(playerIdentity: PlayerIdentity): Boolean =
        db.find { burracoPlayer -> burracoPlayer.identity() == playerIdentity } != null
}

/*
class PlayerFakeAdapter()(implicit val ec: scala.concurrent.ExecutionContext) extends PlayerPort{



  def updatePlayerStatusToPlayerAssigned(playerIdentity: PlayerIdentity): Unit = {
    val idx = PlayerDB.search().indexWhere(p => p.playerIdentity== playerIdentity)
    assert(idx != -1)
    PlayerDB.search().remove(idx)
    PlayerDB.search().addOne(PlayerNotAssigned(playerIdentity).asInstanceOf[BurracoPlayer])
  }

  def mockPlayer():ListBuffer[BurracoPlayer] = {
    PlayerDB.search()
  }
}


 */