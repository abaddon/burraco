package com.abaddon83.burracoGame.readModel.queries

//data class FindBurracoGameExecutionTurnExecutionQuery(val gameIdentity: GameIdentity): Query<BurracoGameExecutionTurnExecution?> {
//}
//
//class FindBurracoGameExecutionTurnExecutionHandler(): QueryHandler<FindBurracoGameExecutionTurnExecutionQuery, BurracoGameExecutionTurnExecution?>, KoinComponent {
//
//    private val repository: BurracoGameReadModelRepositoryPort by inject()
//
//    override fun handle(query: FindBurracoGameExecutionTurnExecutionQuery): BurracoGameExecutionTurnExecution? {
//        return runBlocking { repository.findBurracoGameExecutionTurnExecutionBy(query.gameIdentity) }
//    }
//
//    override suspend fun handleAsync(query: FindBurracoGameExecutionTurnExecutionQuery): BurracoGameExecutionTurnExecution? {
//        return repository.findBurracoGameExecutionTurnExecutionBy(query.gameIdentity)
//    }
//}