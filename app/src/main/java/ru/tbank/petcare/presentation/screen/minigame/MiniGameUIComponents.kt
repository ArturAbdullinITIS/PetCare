package ru.tbank.petcare.presentation.screen.minigame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.model.PetCardUIModel
import java.util.Locale

@Composable
fun CharacterSelectionSection(
    pets: List<PetCardUIModel>,
    selectedPetId: String?,
    onPetSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.select_your_runner),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            items(pets) { pet ->
                val isSelected = pet.id == selectedPetId
                PetRunnerItem(
                    pet = pet,
                    isSelected = isSelected,
                    onClick = { onPetSelected(pet.id) },
                    enabled = !isPlaying
                )
            }
        }
    }
}

@Composable
fun PetRunnerItem(
    pet: PetCardUIModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable(enabled = enabled, onClick = onClick)
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (pet.photoUrl.isNotEmpty()) {
                AsyncImage(
                    model = pet.photoUrl,
                    contentDescription = pet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = pet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = pet.name,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.7f
                )
            }
        )
    }
}

@Composable
fun GameAreaCard(
    state: GameState,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(48.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onTap() }
    ) {
        val petPhotoUrl = state.pets.find { it.id == state.selectedPetId }?.photoUrl
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            val canvasHeightPx = with(LocalDensity.current) { maxHeight.toPx() }.coerceAtLeast(1f)
            val groundYPx = canvasHeightPx - MiniGameViewModel.GROUND_Y_OFFSET

            GameGround()
            GameObstacles(state.obstacles, groundYPx)
            GameRunner(petPhotoUrl, groundYPx, state.runnerY)
        }
        if (!state.isPlaying && !state.isGameOver) {
            GameStartOverlay()
        }

        if (state.isGameOver) {
            GameOverOverlay(state.score, state.highScore, onTap)
        }

        GameScoreOverlay(state.score, state.highScore)
    }
}

@Composable
fun BoxScope.GameGround() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { (MiniGameViewModel.GROUND_Y_OFFSET.coerceAtLeast(0f)).toDp() })
            .align(Alignment.BottomCenter)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val groundY = size.height - MiniGameViewModel.GROUND_Y_OFFSET
        drawLine(
            color = Color.Green.copy(alpha = 0.2f),
            start = androidx.compose.ui.geometry.Offset(0f, groundY),
            end = androidx.compose.ui.geometry.Offset(size.width, groundY),
            strokeWidth = 4f
        )
    }
}

@Composable
fun GameObstacles(obstacles: List<Obstacle>, groundYPx: Float) {
    obstacles.forEach { obs ->
        val treeHeightDp = with(LocalDensity.current) { obs.height.toDp() }
        val treeWidthDp = with(LocalDensity.current) { obs.width.toDp() }
        val treeXDp = with(LocalDensity.current) { obs.x.toDp() }

        val treeTopPx = (groundYPx - obs.height).coerceAtLeast(0f)
        val treeTopDp = with(LocalDensity.current) { treeTopPx.toDp() }

        val treeId = when (obs.type) {
            0 -> R.drawable.small_tree
            1 -> R.drawable.medium_tree
            else -> R.drawable.large_tree
        }

        Image(
            painter = painterResource(id = treeId),
            contentDescription = "Tree",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .absoluteOffset(x = treeXDp, y = treeTopDp)
                .size(width = treeWidthDp, height = treeHeightDp)
        )
    }
}

@Composable
fun GameRunner(petPhotoUrl: String?, groundYPx: Float, runnerYOffsetPixels: Float) {
    val runnerLeft = MiniGameViewModel.RUNNER_X
    val runnerSizePx = MiniGameViewModel.RUNNER_SIZE

    Box(modifier = Modifier.fillMaxSize()) {
        val runnerSizeDp = with(LocalDensity.current) { runnerSizePx.toDp() }
        val runnerTopPx = (groundYPx - runnerSizePx + runnerYOffsetPixels).coerceAtLeast(0f)
        val runnerTopDp = with(LocalDensity.current) { runnerTopPx.toDp() }
        val runnerStartDp = with(LocalDensity.current) { runnerLeft.toDp() }

        val localModifierRunner = Modifier
            .absoluteOffset(x = runnerStartDp, y = runnerTopDp)
            .size(runnerSizeDp)
            .clip(CircleShape)

        if (!petPhotoUrl.isNullOrEmpty()) {
            AsyncImage(
                model = petPhotoUrl,
                contentDescription = "Runner",
                contentScale = ContentScale.Crop,
                modifier = localModifierRunner
            )
        } else {
            Box(
                modifier = localModifierRunner.background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun GameStartOverlay() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_tap),
                contentDescription = stringResource(R.string.tap_to_jump)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.tap_to_jump),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
fun BoxScope.GameOverOverlay(score: Int, highScore: Int, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(32.dp))
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.game_over),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.score, score),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.high_score, highScore),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onTap,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.restart),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun BoxScope.GameScoreOverlay(score: Int, highScore: Int) {
    Column(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .absoluteOffset(x = (-16).dp, y = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = stringResource(R.string.score, String.format(Locale.getDefault(), "%04d", score)),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(R.string.high, highScore),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
