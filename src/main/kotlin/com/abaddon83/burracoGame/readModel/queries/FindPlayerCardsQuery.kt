package com.abaddon83.burracoGame.readModel.queries

//data class FindPlayerCardsQuery(
//        val gameIdentity: GameIdentity,
//        val playerIdentity: PlayerIdentity): Query<List<Card>> {
//}
//
//class FindPlayerCardsHandler(): QueryHandler<FindPlayerCardsQuery, List<Card>>, KoinComponent, WithLog() {
//
//    private val repository: BurracoGameReadModelRepositoryPort by inject()
//
//    override fun handle(query: FindPlayerCardsQuery): List<Card> {
//        return runBlocking {
//            val game = checkNotNull(repository.findBurracoGameExecutionBy(query.gameIdentity)){warnMsg("${query.gameIdentity} not found")}
//            game.playerCards(query.playerIdentity)
//        }
//    }
//
//    override suspend fun handleAsync(query: FindPlayerCardsQuery): List<Card> {
//        val game = checkNotNull(repository.findBurracoGameExecutionBy(query.gameIdentity)){warnMsg("${query.gameIdentity} not found")}
//        return game.playerCards(query.playerIdentity)
//    }
//}