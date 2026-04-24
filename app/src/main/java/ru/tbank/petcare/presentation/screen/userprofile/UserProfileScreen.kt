package ru.tbank.petcare.presentation.screen.userprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.navigation.BottomNavDefaults

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
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        setTopBarActions {
            IconButton(
                onClick = onEditIconClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_profile)
                )
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .padding(bottom = BottomNavDefaults.ContentPadding),
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
