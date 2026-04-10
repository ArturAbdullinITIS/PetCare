package ru.tbank.petcare.presentation.screen.userprofile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.presentation.common.EditPhotoIcon

@Composable
fun UserProfileScreen(
    onLogoutSuccess: () -> Unit,
    onEditIconClick: () -> Unit,
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
    onSettingsClick: () -> Unit,
) {
    UserProfileContent(
        onLogoutSuccess = onLogoutSuccess,
        setTopBarActions = setTopBarActions,
        onEditIconClick = onEditIconClick,
        onSettingsClick = onSettingsClick,
    )
}

@Suppress("LongParameterList")
@Composable
fun UserProfileContent(
    modifier: Modifier = Modifier,
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
    onEditIconClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        setTopBarActions {
            EditPhotoIcon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = onEditIconClick)
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            UserProfileHeader(
                name = state.name,
                email = state.email,
                avatarUrl = state.avatarUrl
            )

            Spacer(modifier = Modifier.height(28.dp))

            StatsGridSection(
                numberOfPets = state.numberOfPets,
                bestScore = state.bestScore
            )

            Spacer(modifier = Modifier.height(28.dp))

            MenuActionsSection(
                onSettingsClick = onSettingsClick,
                onLogoutClick = {
                    viewModel.processCommand(UserProfileCommand.Logout)
                    onLogoutSuccess()
                }
            )
        }
    }
}
