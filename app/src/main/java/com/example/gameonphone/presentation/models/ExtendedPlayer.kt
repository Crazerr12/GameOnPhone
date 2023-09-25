package com.example.gameonphone.presentation.models

class ExtendedPlayer() {
    fun heal(player: PlayerModel) {
        player.currentHealth = player.currentHealth + (player.maximumHealth * 30 / 100)
        if (player.currentHealth > player.maximumHealth) player.currentHealth = player.maximumHealth
    }
}