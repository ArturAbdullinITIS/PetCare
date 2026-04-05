package ru.tbank.petcare.presentation.screen.settings

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.ui.theme.GroomingQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.VetQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.WalkQuickActionIcon

@Composable
fun SettingsScreen() {
    SettingContent()
}

@Composable
private fun SettingContent(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    Card(
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
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
                    viewModel.processCommand(SettingsCommand.EnableAllNotifications(it))
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
                        viewModel.processCommand(SettingsCommand.EnableGroomingNotifications(it))
                    }
                )
                NotificationsSwitch(
                    icon = VetQuickActionIcon,
                    text = stringResource(R.string.veterinarian),
                    enabled = state.settingsConfig.notifications.vet,
                    onEnable = {
                        viewModel.processCommand(SettingsCommand.EnableVetNotifications(it))
                    }
                )
                NotificationsSwitch(
                    icon = WalkQuickActionIcon,
                    text = stringResource(R.string.walk),
                    enabled = state.settingsConfig.notifications.walk,
                    onEnable = {
                        viewModel.processCommand(SettingsCommand.EnableWalkNotifications(it))
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
                    language = state.settingsConfig.language,
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
                onClick = {}
            )
        }
    }
}
