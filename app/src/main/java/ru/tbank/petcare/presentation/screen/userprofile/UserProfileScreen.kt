package ru.tbank.petcare.presentation.screen.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.EditPhotoIcon
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme

@Composable
fun UserProfileScreen(
    onLogoutSuccess: () -> Unit, onEditIconClick: () -> Unit,
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


