package com.abaddon83.burracoGame.commandModel.ports

import com.abaddon83.burracoGame.commandModel.models.BurracoGame
import com.abaddon83.burracoGame.commandModel.models.games.GameIdentity
import com.abaddon83.utils.es.repository.Repository

interface BurracoGameRepositoryPort: Repository<GameIdentity, BurracoGame> {
}