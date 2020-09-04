package com.abaddon83.burracoGame.readModel.models

//import com.abaddon83.burracoGame.commandModel.adapters.commandControllerRestAdapters.messages.CardModule
//import com.abaddon83.burracoGame.commandModel.models.BurracoGameCreated
//import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.GameStarted
//import com.abaddon83.burracoGame.commandModel.models.burracoGameWaitingPlayers.PlayerAdded
//import com.abaddon83.burracoGame.readModel.ports.DocumentStorePort
//import com.abaddon83.utils.es.Event
//import com.abaddon83.utils.es.messageBus.Handles
//import com.abaddon83.utils.es.readModel.DocumentStore
//import com.abaddon83.utils.logs.WithLog
//import org.koin.core.KoinComponent
//import org.koin.core.inject
//import java.util.*
//import kotlin.NoSuchElementException
//
//
//data class BurracoGame(
//        val identity: UUID,
//        val status: String,
//        val players: List<BurracoPlayer>,
//        val playerTurn: BurracoPlayer? = null,
//        val numMazzettoAvailable: Int = 0,
//        val discardPile: List<Card>? = listOf()
//)
//
//class BurracoGameReadModel(private val burracoGameStore: DocumentStore<BurracoGame>) {
//
//    fun find(key: String): BurracoGame = burracoGameStore.get(key)?: throw NoSuchElementException("Key not found")
//}
//
//
//class BurracoGameProjection() : Handles<Event>, KoinComponent, WithLog() {
//    private val burracoGameStore: DocumentStore<BurracoGame> by inject()
//    override fun handle(event: Event) {
//        when (event) {
//            is BurracoGameCreated -> {
//                val new = event.toBurracoGame()
//                log.info("Add new Burraco game {} to the BurracoGameReadModel", new)
//                check(burracoGameStore.get(event.identity)== null){errorMsg("The game identity with id: ${event.identity} already exist!! duplicated events??")}
//                burracoGameStore.save(event.identity,event.toBurracoGame())
//            }
//            is PlayerAdded -> {
//                val burracoGame = checkNotNull(burracoGameStore.get(event.gameIdentity)){ errorMsg("Received event ${event::class.simpleName} but the game key ${event.gameIdentity} is not found")}
//                val updatedBurracoGame = burracoGame.copy( players = burracoGame.players.plus(event.getBurracoPlayer()))
//                burracoGameStore.save(event.gameIdentity,updatedBurracoGame)
//            }
//            is GameStarted -> {
//                val burracoGame = checkNotNull(burracoGameStore.get(event.gameIdentity)){ errorMsg("Received event ${event::class.simpleName} but the game key ${event.gameIdentity} is not found")}
//                val updatedBurracoGame = burracoGame.copy(
//                        status = "Execution",
//                        playerTurn = BurracoPlayer(identity = UUID.fromString(event.playerIdentityTurn)),
//                        numMazzettoAvailable = 2,
//                        discardPile = event.discardPileCards.map { c -> Card(Suit.valueOf(c.suit), Rank.valueOf(c.rank))}
//                )
//                burracoGameStore.save(event.gameIdentity,updatedBurracoGame)
//            }
//        }
//    }
//
//    private fun BurracoGameCreated.toBurracoGame(): BurracoGame = BurracoGame(
//            identity = UUID.fromString(this.identity),
//            status = "WAITING",
//            players = listOf())
//
//    private fun PlayerAdded.getBurracoPlayer(): BurracoPlayer = BurracoPlayer(identity = UUID.fromString(this.playerIdentity))
//
//
//
//}
