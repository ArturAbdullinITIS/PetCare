package ru.tbank.petcare.presentation.screen.settings

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.ConfirmDeleteDialog
import ru.tbank.petcare.presentation.ui.theme.GroomingQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.VetQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.WalkQuickActionIcon

@Composable
fun SettingsScreen(
    onDeleteClick: () -> Unit
) {
    SettingContent(
        onDeleteClick = onDeleteClick
    )
}

@Composable
private fun SettingContent(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var pendingCommand by remember { mutableStateOf<SettingsCommand?>(null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                pendingCommand?.let(viewModel::processCommand)
            } else {
                pendingCommand = null
            }
        }
    )

    fun handleNotificationCommand(command: SettingsCommand) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                viewModel.processCommand(command)
            } else {
                pendingCommand = command
                permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            viewModel.processCommand(command)
        }
    }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                showDeleteDialog = false
                viewModel.processCommand(SettingsCommand.DeleteProfile)
            },
            onDismiss = { showDeleteDialog = false },
            title = stringResource(R.string.delete_your_account),
            text = stringResource(R.string.you_can_not_restore_it),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.Error -> {}
                SettingsEvent.ProfileDeleted -> {
                    onDeleteClick()
                }
            }
        }
    }

    Card(
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsTitle(
                title = stringResource(R.string.notification_settings),
                content = {
                    NotificationsIcon()
                }
            )
            AllNotificationsSwitch(
                enabled = state.settingsConfig.notifications.enabled,
                onEnable = {
                    handleNotificationCommand(SettingsCommand.EnableAllNotifications(it))
                }
            )
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                NotificationsSwitch(
                    icon = GroomingQuickActionIcon,
                    text = stringResource(R.string.grooming),
                    enabled = state.settingsConfig.notifications.grooming,
                    onEnable = {
                        handleNotificationCommand(SettingsCommand.EnableGroomingNotifications(it))
                    }
                )
                NotificationsSwitch(
                    icon = VetQuickActionIcon,
                    text = stringResource(R.string.veterinarian),
                    enabled = state.settingsConfig.notifications.vet,
                    onEnable = {
                        handleNotificationCommand(SettingsCommand.EnableVetNotifications(it))
                    }
                )
                NotificationsSwitch(
                    icon = WalkQuickActionIcon,
                    text = stringResource(R.string.walk),
                    enabled = state.settingsConfig.notifications.walk,
                    onEnable = {
                        handleNotificationCommand(SettingsCommand.EnableWalkNotifications(it))
                    }
                )
            }
            HorizontalDivider(modifier = Modifier.padding(16.dp))
            SettingsTitle(
                title = stringResource(R.string.appearance),
                content = {
                    AppearanceIcon()
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.theme),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ThemeSegmentedControl(
                    darkTheme = state.settingsConfig.darkTheme,
                    onSelected = {
                        viewModel.processCommand(SettingsCommand.EnableDarkTheme(it))
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                LanguageSegmentedControl(
                    languageState = state.languageState,
                    onSelected = {
                        viewModel.processCommand(SettingsCommand.ChangeLanguage(it))
                    }
                )
            }
            HorizontalDivider(modifier = Modifier.padding(16.dp))
            SettingsTitle(
                title = stringResource(R.string.account_management),
                content = {
                    AccountManagementIcon()
                }
            )
            DeleteAccountButton(
                onClick = {
                    showDeleteDialog = true
                }
            )
        }
    }
}
