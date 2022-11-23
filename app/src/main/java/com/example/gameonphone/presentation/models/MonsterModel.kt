package com.example.gameonphone.presentation.models

data class MonsterModel(
    var health: Int, // 0-N
    var attack: IntRange, // 1-20
    var armor: Int   // 1-20
)
