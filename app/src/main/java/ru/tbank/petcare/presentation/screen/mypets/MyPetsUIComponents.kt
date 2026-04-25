package ru.tbank.petcare.presentation.screen.mypets

import android.R.attr.fontWeight
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.IconStatusUI
import ru.tbank.petcare.presentation.mapper.getQuickActionUI
import ru.tbank.petcare.presentation.model.LastActivityUIModel
import ru.tbank.petcare.presentation.model.PetCardUIModel
import ru.tbank.petcare.presentation.model.QuickActionType
import ru.tbank.petcare.presentation.model.QuickActionUIModel
import ru.tbank.petcare.presentation.ui.theme.PetTipsIcon

@Composable
fun MyPetsPetCard(
    pet: PetCardUIModel,
    onPetClick: () -> Unit,
    clickable: Boolean,
    model: LastActivityUIModel?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            if (clickable) onPetClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(80.dp)
        ) {
            Box {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Box(modifier = Modifier.padding(3.dp)) {
                        AsyncImage(
                            model = pet.photoUrl,
                            contentDescription = stringResource(R.string.pets_photo),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            placeholder = painterResource(R.drawable.photo_placeholder),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.photo_placeholder)
                        )
                    }
                }

                IconStatusUI(
                    status = pet.iconStatus,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp),
                    iconSize = 24
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,

            ) {
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pet.subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (model != null) {
                    LastActivityCard(
                        model = model
                    )
                }
            }
        }
    }
}

@Composable
fun TipCard(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = PetTipsIcon,
                contentDescription = stringResource(R.string.pet_health_tip)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.pet_health_tip),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                AnimatedContent(
                    targetState = text,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                    }
                ) { targetText ->
                    Text(
                        text = targetText,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    model: QuickActionUIModel,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Card(
        shape = RoundedCornerShape(36.dp),
        modifier = modifier.aspectRatio(1.1f),
        colors = CardDefaults.cardColors(
            containerColor = model.containerColor,
        ),
        onClick = {
            if (enabled) onClick()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = model.icon,
                contentDescription = stringResource(R.string.quick_action, model.title),
                tint = model.contentColor
            )
            Text(
                text = stringResource(model.title),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = model.contentColor
            )
        }
    }
}

@Composable
fun QuickActionRow(
    onWalkClick: () -> Unit,
    onGroomingClick: () -> Unit,
    onVetClick: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuickActionCard(
            modifier = Modifier.weight(1f),
            model = getQuickActionUI(QuickActionType.WALK),
            onClick = onWalkClick,
            enabled = enabled
        )
        QuickActionCard(
            modifier = Modifier.weight(1f),
            model = getQuickActionUI(QuickActionType.GROOMING),
            onClick = onGroomingClick,
            enabled = enabled
        )
        QuickActionCard(
            modifier = Modifier.weight(1f),
            model = getQuickActionUI(QuickActionType.VET),
            onClick = onVetClick,
            enabled = enabled
        )
    }
}

@Composable
fun QuickActionsTitleRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.quick_actions_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.create_activity).uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun YourFamilyTitle() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.your_family_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun EmptyPetsTitle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.add_your_first_pet_to_get_started),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun LastActivityCard(
    modifier: Modifier = Modifier,
    model: LastActivityUIModel
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = model.colors.bg.copy(alpha = 0.5f),
            contentColor = model.colors.fg
        ),
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(
            width = 1.dp,
            color = model.colors.bg.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = model.icon,
                contentDescription = stringResource(R.string.last_activity_icon)
            )
            Text(
                text = model.text.uppercase(),
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
