package com.abaddon83.burracoGame.adapters.burracoGameRepositoryAdapters.inMemories

import com.abaddon83.burracoGame.domainModels.BurracoGame
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecution
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnBeginning
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnEnd
import com.abaddon83.burracoGame.domainModels.burracoGameExecutions.BurracoGameExecutionTurnExecution
import com.abaddon83.burracoGame.domainModels.burracoGameWaitingPlayers.BurracoGameWaitingPlayers
import com.abaddon83.burracoGame.domainModels.burracoGameendeds.BurracoGameEnded
import com.abaddon83.burracoGame.ports.BurracoGameRepositoryPort
import com.abaddon83.burracoGame.shared.games.GameIdentity
import com.abaddon83.utils.logs.WithLog

class BurracoGameRepositoryInMemoryAdapter : BurracoGameRepositoryPort, WithLog() {
    override fun save(burracoGame: BurracoGameWaitingPlayers): BurracoGameWaitingPlayers =
            when (val game = BurracoGameDB.persist(burracoGame)) {
                is BurracoGameWaitingPlayers -> game
                else -> { throw NoSuchElementException() }
            }

    override fun save(burracoGame: BurracoGameExecutionTurnBeginning): BurracoGameExecutionTurnBeginning =
            when (val game = BurracoGameDB.persist(burracoGame)) {
                is BurracoGameExecutionTurnBeginning -> game
                else -> throw NoSuchElementException()
            }

    override fun save(burracoGame: BurracoGameExecutionTurnExecution): BurracoGameExecutionTurnExecution =
            when (val game = BurracoGameDB.persist(burracoGame)) {
                is BurracoGameExecutionTurnExecution -> game
                else -> throw NoSuchElementException()
            }

    override fun save(burracoGame: BurracoGameExecutionTurnEnd): BurracoGameExecutionTurnEnd =
            when (val game = BurracoGameDB.persist(burracoGame)) {
                is BurracoGameExecutionTurnEnd -> game
                else -> throw NoSuchElementException()
            }

    override fun save(burracoGame: BurracoGameExecution): BurracoGameExecution =
            when (val game = BurracoGameDB.persist(burracoGame)) {
                is BurracoGameExecution -> game
                else -> throw NoSuchElementException()
            }

    override fun save(burracoGame: BurracoGameEnded): BurracoGameEnded =
            when (val game = BurracoGameDB.persist(burracoGame)) {
                is BurracoGameEnded -> game
                else -> throw NoSuchElementException()
            }

    override fun exists(identity: GameIdentity): Boolean =
            BurracoGameDB.search().find { game -> game.identity() == identity } != null

    override suspend fun findBurracoGameBy(gameIdentity: GameIdentity): BurracoGame? =
        BurracoGameDB.search().find { game -> game.identity() == gameIdentity }

    override suspend fun findBurracoGameWaitingPlayersBy(gameIdentity: GameIdentity): BurracoGameWaitingPlayers? =
            BurracoGameDB.search().find { game -> game.identity() == gameIdentity } as BurracoGameWaitingPlayers?

    override suspend fun findBurracoGameExecutionBy(gameIdentity: GameIdentity): BurracoGameExecution? =
            BurracoGameDB.search().find { game -> game.identity() == gameIdentity } as BurracoGameExecution


    override suspend fun findBurracoGameExecutionTurnBeginningBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnBeginning? =
            BurracoGameDB.search().find { game -> game.identity() == gameIdentity } as BurracoGameExecutionTurnBeginning?

    override suspend fun findBurracoGameExecutionTurnExecutionBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnExecution? =
            BurracoGameDB.search().find { game -> game.identity() == gameIdentity } as BurracoGameExecutionTurnExecution?


    override suspend fun findBurracoGameExecutionTurnEndBy(gameIdentity: GameIdentity): BurracoGameExecutionTurnEnd? =
            BurracoGameDB.search().find { game -> game.identity() == gameIdentity } as BurracoGameExecutionTurnEnd?

    override suspend fun findBurracoGameEndedBy(gameIdentity: GameIdentity): BurracoGameEnded? =
            BurracoGameDB.search().find { game -> game.identity() == gameIdentity } as BurracoGameEnded?

    override suspend fun findAllBurracoGameWaitingPlayers(): List<BurracoGameWaitingPlayers>? =
            BurracoGameDB.search().filterIsInstance<BurracoGameWaitingPlayers>()
}

object BurracoGameDB: WithLog(){
    private val db = mutableListOf<BurracoGame>()

    fun persist(burracoGame: BurracoGame): BurracoGame {
        when (isAnUpdate(burracoGame.identity())) {
            true -> update(burracoGame)
            false -> add(burracoGame)
        }
        return burracoGame
    }

    fun search(): MutableList<BurracoGame> = db;

    private fun add(burracoGame: BurracoGame): Unit {
        when(db.add(burracoGame)){
            false -> log.error("[${object{}.javaClass.enclosingMethod?.name}]: saved failed. ${burracoGame.identity()}")

        }
    }

    private fun update(burracoGame: BurracoGame): Unit {
        val gameToRemove = checkNotNull(db.find { game -> game.identity() == burracoGame.identity() }){
            log.error("[${object{}.javaClass.enclosingMethod?.name}]: Is not possible update a burraco game that doesn't exist. ${burracoGame.identity()}")
        }
        db.remove(gameToRemove)
        db.add(burracoGame)

    }

    private fun isAnUpdate(gameIdentity: GameIdentity): Boolean =
            db.find { burracoGame -> burracoGame.identity() == gameIdentity } != null
}