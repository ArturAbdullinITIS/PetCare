package ru.tbank.petcare.presentation.screen.onboarding

import android.R.attr.fontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter.State.Empty.painter
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.model.OnboardingPageUIModel
import ru.tbank.petcare.presentation.ui.theme.GroomingQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme
import ru.tbank.petcare.presentation.ui.theme.VetQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.WalkQuickActionIcon

@Composable
fun OnboardingPageContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    model: OnboardingPageUIModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(model.titleRes),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(model.subtitleRes),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(model.descriptionRes),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WelcomeContentCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(48.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(262.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.welcome_page),
                contentDescription = stringResource(R.string.welcome_image),
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Composable
fun TrackCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(48.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiniCard(
                    modifier = Modifier.weight(1f),
                    bg = MaterialTheme.colorScheme.primaryContainer,
                    fg = MaterialTheme.colorScheme.onPrimaryContainer,
                    icon = {
                        Icon(
                            imageVector = WalkQuickActionIcon,
                            contentDescription = stringResource(R.string.walks_stat)
                        )
                    },
                    title = stringResource(R.string.daily_walks).uppercase()
                )
                MiniCard(
                    modifier = Modifier.weight(1f),
                    bg = MaterialTheme.colorScheme.tertiaryContainer,
                    fg = MaterialTheme.colorScheme.onTertiaryContainer,
                    icon = {
                        Icon(
                            imageVector = GroomingQuickActionIcon,
                            contentDescription = stringResource(R.string.grooming_stat)
                        )
                    },
                    title = stringResource(R.string.grooming_plans).uppercase()
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiniCard(
                    modifier = Modifier.weight(1f),
                    bg = MaterialTheme.colorScheme.tertiaryContainer,
                    fg = MaterialTheme.colorScheme.onTertiaryContainer,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = stringResource(R.string.analytics_stats)
                        )
                    },
                    title = stringResource(R.string.smart_stats).uppercase()
                )
                MiniCard(
                    modifier = Modifier.weight(1f),
                    bg = MaterialTheme.colorScheme.secondaryContainer,
                    fg = MaterialTheme.colorScheme.onSecondaryContainer,
                    icon = {
                        Icon(
                            imageVector = VetQuickActionIcon,
                            contentDescription = stringResource(R.string.vet_stats)
                        )
                    },
                    title = stringResource(R.string.vet_visit).uppercase()
                )
            }
        }
    }
}

@Composable
fun ConnectCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(48.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(72.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiniCard(
                    modifier = Modifier.weight(1f),
                    bg = MaterialTheme.colorScheme.primaryContainer,
                    fg = MaterialTheme.colorScheme.onPrimaryContainer,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_public),
                            contentDescription = stringResource(R.string.walks_stat)
                        )
                    },
                    title = stringResource(R.string.public_pets).uppercase()
                )

                MiniCard(
                    modifier = Modifier.weight(1f),
                    bg = MaterialTheme.colorScheme.tertiaryContainer,
                    fg = MaterialTheme.colorScheme.onTertiaryContainer,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_pet_runner),
                            contentDescription = stringResource(R.string.walks_stat)
                        )
                    },
                    title = stringResource(R.string.pet_runner).uppercase()
                )
            }
        }
    }
}

@Composable
fun MiniCard(
    modifier: Modifier = Modifier,
    bg: Color,
    fg: Color,
    icon: @Composable () -> Unit,
    title: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = bg,
            contentColor = fg
        ),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            icon()
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
fun MiniCardPreview() {
    PetCareTheme {
        MiniCard(
            bg = MaterialTheme.colorScheme.primaryContainer,
            fg = MaterialTheme.colorScheme.onPrimaryContainer,
            icon = {
                Icon(
                    imageVector = WalkQuickActionIcon,
                    contentDescription = stringResource(R.string.walks_stat)
                )
            },
            title = "Walk stats"
        )
    }
}

@Composable
fun OnboardingPagerIndicator(
    currentPage: Int,
    pageCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val isSelected = currentPage == index

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(8.dp)
                    .width(if (isSelected) 24.dp else 8.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
                        }
                    )
            )
        }
    }
}
