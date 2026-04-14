package ru.tbank.petcare.presentation.screen.minigame

import android.util.Log
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.pets.GetAllPetsUseCase
import ru.tbank.petcare.domain.usecase.pets.UpdatePetHighScoreUseCase
import ru.tbank.petcare.presentation.mapper.toPetCardUIModel
import javax.inject.Inject

@HiltViewModel
class MiniGameViewModel @Inject constructor(
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val updatePetHighScoreUseCase: UpdatePetHighScoreUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    init {
        loadPets()
    }

    fun processCommand(command: MiniGameCommand) {
        when (command) {
            is MiniGameCommand.UpdateFrame -> updateGame(command.deltaTime)
            MiniGameCommand.Jump -> jump()
            MiniGameCommand.Restart -> restart()
            MiniGameCommand.StartGame -> startGame()
            is MiniGameCommand.SelectPet -> selectPet(command.petId)
            MiniGameCommand.UpdateHighScore -> updateHighScore()
        }
    }

    private fun loadPets() {
        viewModelScope.launch {
            getAllPetsUseCase().collect { pets ->
                val uiPets = pets.map { it.toPetCardUIModel() }
                _state.update { currentState ->
                    val selectedId = uiPets.firstOrNull()?.id ?: currentState.selectedPetId
                    val petScore = pets.find { it.id == selectedId }?.gameScore ?: 0
                    currentState.copy(
                        pets = uiPets,
                        selectedPetId = selectedId,
                        highScore = petScore
                    )
                }
            }
        }
    }

    private fun selectPet(petId: String) {
        if (_state.value.isPlaying && !_state.value.isGameOver) return
        viewModelScope.launch {
            getAllPetsUseCase().collect { pets ->
                val petScore = pets.find { it.id == petId }?.gameScore ?: 0
                _state.update {
                    it.copy(
                        selectedPetId = petId,
                        highScore = petScore
                    )
                }
            }
        }
    }

    private fun updateHighScore() {
        _state.update {
                state ->
            Log.d("MiniGameViewModel", "updateHighScore. Score: ${state.score}, HighScore: ${state.highScore}")
            if (state.score == state.highScore) {
                viewModelScope.launch {
                    updatePetHighScoreUseCase(state.selectedPetId ?: "", state.score)
                }
            }
            state.copy(highScore = state.highScore)
        }
    }

    private fun startGame() {
        _state.update {
            it.copy(
                isPlaying = true,
                isGameOver = false,
                score = 0,
                exactScore = 0f,
                runnerY = 0f,
                runnerVelocity = -JUMP_VELOCITY,
                obstacles = emptyList(),
                worldSpeed = 600f,
                spawnTimer = 0f,
                nextSpawnDelay = 1.8f + (Math.random().toFloat() * 2.0f)
            )
        }
    }

    private fun updateGame(deltaTime: Float) {
        val currentState = _state.value
        if (currentState.isGameOver || !currentState.isPlaying) return

        var newVelocity = currentState.runnerVelocity + GRAVITY * deltaTime
        var newY = currentState.runnerY + newVelocity * deltaTime

        if (newY > 0f) {
            newY = 0f
            newVelocity = 0f
        }

        val newObstacles = currentState.obstacles.map {
            it.copy(x = it.x - currentState.worldSpeed * deltaTime)
        }.filter { it.x > -1000f }.toMutableList()

        var newSpawnTimer = currentState.spawnTimer + deltaTime
        var newNextSpawnDelay = currentState.nextSpawnDelay

        if (newSpawnTimer > newNextSpawnDelay) {
            newSpawnTimer = 0f
            newNextSpawnDelay = 2.0f + (Math.random().toFloat() * 2.5f)
            val randomType = (0..2).random()
            val treeHeight = when (randomType) {
                0 -> 180f
                1 -> 240f
                else -> 300f
            }
            newObstacles.add(Obstacle(x = 1500f, y = 0f, width = 120f, height = treeHeight, type = randomType))
        }

        var newExactScore = currentState.exactScore + (deltaTime * 10f)
        val newScore = newExactScore.toInt()
        var gameOver = false

        val runnerRect = Rect(RUNNER_X + 20f, -newY - RUNNER_SIZE + 20f, RUNNER_X + RUNNER_SIZE - 20f, -newY - 20f)

        for (i in newObstacles.indices) {
            val obs = newObstacles[i]
            val obsRect = Rect(obs.x + 20f, -obs.height + 20f, obs.x + obs.width - 20f, -20f)

            if (runnerRect.overlaps(obsRect)) {
                gameOver = true
            }

            if (!obs.passed && obs.x + obs.width < RUNNER_X) {
                newObstacles[i] = obs.copy(passed = true)
            }
        }

        val newHighScore = if (newScore > currentState.highScore) newScore else currentState.highScore

        _state.update {
            it.copy(
                runnerY = newY,
                runnerVelocity = newVelocity,
                obstacles = newObstacles,
                spawnTimer = newSpawnTimer,
                nextSpawnDelay = newNextSpawnDelay,
                score = newScore,
                exactScore = newExactScore,
                highScore = newHighScore,
                isGameOver = gameOver,
                worldSpeed = it.worldSpeed + 5f * deltaTime
            )
        }
    }

    private fun jump() {
        if (_state.value.runnerY >= -10f && _state.value.isPlaying && !_state.value.isGameOver) {
            _state.update { it.copy(runnerVelocity = -JUMP_VELOCITY) }
        } else if (!_state.value.isPlaying && !_state.value.isGameOver) {
            startGame()
        }
    }

    private fun restart() {
        _state.update {
            it.copy(
                isPlaying = false,
                isGameOver = false,
                score = 0,
                exactScore = 0f,
                highScore = it.highScore,
                runnerY = 0f,
                runnerVelocity = 0f,
                obstacles = emptyList(),
                worldSpeed = 600f,
                spawnTimer = 0f,
                nextSpawnDelay = 2.0f + (Math.random().toFloat() * 2.5f)
            )
        }
    }

    companion object {
        const val GRAVITY = 5500f
        const val JUMP_VELOCITY = 2400f
        const val RUNNER_X = 200f
        const val RUNNER_SIZE = 200f
        const val GROUND_Y_OFFSET = 120f
    }
}

sealed interface MiniGameCommand {
    data class UpdateFrame(val deltaTime: Float) : MiniGameCommand
    object Jump : MiniGameCommand
    object Restart : MiniGameCommand
    object StartGame : MiniGameCommand
    data class SelectPet(val petId: String) : MiniGameCommand
    object UpdateHighScore : MiniGameCommand
}
