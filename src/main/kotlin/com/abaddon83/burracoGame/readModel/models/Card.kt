package com.abaddon83.burracoGame.readModel.models

data class Card(
        val suit: Suit,
        val rank:Rank
)

enum class Suit () {Heart, Tile, Clover, Pike, Jolly}

enum class Rank () {Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Jolly}