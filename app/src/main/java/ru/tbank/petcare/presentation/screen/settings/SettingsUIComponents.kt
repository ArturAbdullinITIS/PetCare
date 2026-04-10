package ru.tbank.petcare.presentation.screen.settings

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.Language

private const val FRACTION = 0.6f

@Composable
fun SettingsTitle(
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        content()
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NotificationsIcon() {
    Surface(
        shape = CircleShape,
        modifier = Modifier.size(40.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.notifications_icon),
                modifier = Modifier.fillMaxSize(FRACTION)
            )
        }
    }
}

@Composable
fun AppearanceIcon() {
    Surface(
        shape = CircleShape,
        modifier = Modifier.size(40.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = stringResource(R.string.appearance_icon),
                modifier = Modifier.fillMaxSize(FRACTION)
            )
        }
    }
}

@Composable
fun AccountManagementIcon() {
    Surface(
        shape = CircleShape,
        modifier = Modifier.size(40.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.ManageAccounts,
                contentDescription = stringResource(R.string.account_management_icon),
                modifier = Modifier.fillMaxSize(FRACTION)
            )
        }
    }
}

@Composable
fun AllNotificationsSwitch(
    enabled: Boolean,
    onEnable: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.all_notifications),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = enabled,
                onCheckedChange = onEnable,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    checkedThumbColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

@Composable
fun NotificationsSwitch(
    icon: ImageVector,
    text: String,
    enabled: Boolean,
    onEnable: (Boolean) -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VerticalDivider(
                modifier = Modifier.height(64.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.activity_type_icon)
            )
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = enabled,
                onCheckedChange = onEnable,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

@Composable
fun ThemeSegmentedControl(
    darkTheme: Boolean,
    onSelected: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .height(36.dp)
                .width(188.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                modifier = Modifier
                    .weight(1f),
                onClick = { onSelected(false) },
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (darkTheme) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    containerColor = if (darkTheme) Color.Transparent else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.light),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            Spacer(Modifier.width(6.dp))

            TextButton(
                modifier = Modifier
                    .weight(1f),
                onClick = { onSelected(true) },
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (!darkTheme) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    containerColor = if (!darkTheme) Color.Transparent else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.dark),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun LanguageSegmentedControl(
    languageState: LanguageState,
    onSelected: (Language) -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .height(36.dp)
                .width(188.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                modifier = Modifier
                    .weight(1f),
                onClick = { onSelected(Language.RUSSIAN) },
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (languageState.ruSelected) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    },
                    containerColor = if (languageState.ruSelected) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        Color.Transparent
                    }
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.russian),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            Spacer(Modifier.width(6.dp))

            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 0.dp),
                onClick = { onSelected(Language.ENGLISH) },
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (languageState.enSelected) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    },
                    containerColor = if (languageState.enSelected) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        Color.Transparent
                    }
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.english),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun DeleteAccountButton(
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = stringResource(R.string.delete_account_icon)
            )
            Text(
                text = stringResource(R.string.delete_account),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.arrow_forward_keyboard_icon)
            )
        }
    }
}
