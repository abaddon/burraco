package com.abaddon83.burracoGame.readModel.adapters.burracoGameReadModelRepositoryAdapters

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

class BurracoGameTable : Table() {
    val identity: Column<UUID> = uuid("identity")

}