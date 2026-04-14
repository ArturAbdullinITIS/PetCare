package ru.tbank.petcare.presentation.screen.minigame

import ru.tbank.petcare.presentation.model.PetCardUIModel

data class GameState(
    val runnerY: Float = 0f,
    val runnerVelocity: Float = 0f,
    val obstacles: List<Obstacle> = emptyList(),
    val score: Int = 0,
    val exactScore: Float = 0f,
    val highScore: Int = 0,
    val isGameOver: Boolean = false,
    val isPlaying: Boolean = false,
    val worldSpeed: Float = 600f,
    val spawnTimer: Float = 0f,
    val nextSpawnDelay: Float = 2f,
    val pets: List<PetCardUIModel> = emptyList(),
    val selectedPetId: String? = null
)

data class Obstacle(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val type: Int = 0,
    val passed: Boolean = false
)
