package ru.tbank.petcare.presentation.screen.minigame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.presentation.navigation.BottomNavDefaults

@Composable
fun MiniGameScreen() {
    MiniGameContent()
}

@Composable
fun MiniGameContent(
    viewModel: MiniGameViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(state.isPlaying) {
        if (state.isPlaying) {
            var lastTime = 0L
            while (true) {
                withFrameMillis { currentTime ->
                    if (lastTime == 0L) lastTime = currentTime
                    val deltaTime = (currentTime - lastTime) / 1000f
                    lastTime = currentTime
                    viewModel.processCommand(MiniGameCommand.UpdateFrame(deltaTime))
                }
            }
        }
    }

    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) {
            viewModel.processCommand(MiniGameCommand.UpdateHighScore)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = BottomNavDefaults.ContentPadding)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            CharacterSelectionSection(
                pets = state.pets,
                selectedPetId = state.selectedPetId,
                isPlaying = state.isPlaying && !state.isGameOver,
                onPetSelected = {
                    if (!state.isPlaying) {
                        viewModel.processCommand(MiniGameCommand.SelectPet(it))
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GameAreaCard(
                state = state,
                onTap = {
                    if (state.isGameOver) {
                        viewModel.processCommand(MiniGameCommand.Restart)
                    } else if (state.isPlaying) {
                        viewModel.processCommand(MiniGameCommand.Jump)
                    } else {
                        viewModel.processCommand(MiniGameCommand.StartGame)
                    }
                },
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}
