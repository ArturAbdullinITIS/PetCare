package ru.tbank.petcare.presentation.screen.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.tbank.petcare.R

@Composable
fun UserProfileHeader(
    name: String,
    email: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val gradient = Brush.linearGradient(
            colors = listOf(Color(0xFFA8D8C9), Color(0xFFE8F5F1))
        )

        Box(
            modifier = Modifier
                .size(156.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(40.dp),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
                .background(Color.White, RoundedCornerShape(40.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = stringResource(R.string.profile_avatar),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(32.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient, RoundedCornerShape(32.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = name,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            letterSpacing = (-0.7).sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = email,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun StatsGridSection(
    numberOfPets: Int,
    bestScore: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            value = numberOfPets.toString().padStart(2, '0'),
            label = stringResource(R.string.number_of_pets),
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
            valueColor = MaterialTheme.colorScheme.onSecondary,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.weight(1f)
        )

        StatCard(
            value = bestScore.toString(),
            label = stringResource(R.string.best_score),
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            valueColor = MaterialTheme.colorScheme.onSecondary,
            labelColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
            modifier = Modifier.weight(1f)
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun StatCard(
    value: String,
    label: String,
    backgroundColor: Color,
    valueColor: Color,
    labelColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(150.dp)
            .background(backgroundColor, RoundedCornerShape(32.dp))
            .padding(vertical = 24.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = valueColor,
                lineHeight = 40.sp
            )
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = labelColor
            )
        }
    }
}

@Composable
fun MenuActionsSection(
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MenuActionButton(
            title = stringResource(R.string.settings),
            subtitle = stringResource(R.string.settings_discription),
            iconResId = R.drawable.ic_settings,
            iconContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
            titleColor = MaterialTheme.colorScheme.onSurface,
            onClick = onSettingsClick
        )

        MenuActionButton(
            title = stringResource(R.string.logout),
            subtitle = null,
            iconResId = R.drawable.ic_logout,
            iconContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            titleColor = Color.Red.copy(alpha = 0.8f),
            onClick = onLogoutClick
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun MenuActionButton(
    title: String,
    subtitle: String?,
    iconResId: Int,
    iconContainerColor: Color,
    titleColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(iconContainerColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = titleColor
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = stringResource(R.string.arrow_forward),
                modifier = Modifier.size(16.dp),
                tint = Color.Red.copy(alpha = 0.3f)
            )
        }
    }
}
