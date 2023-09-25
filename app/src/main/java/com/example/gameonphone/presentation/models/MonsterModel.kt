package com.example.gameonphone.presentation.models

data class MonsterModel(
    var currentHealth: Int, // 0-N
    var maximumHealth: Int, // 0-N
    var attack: IntRange, // 1-30
    var armor: Int   // 1-30
)
