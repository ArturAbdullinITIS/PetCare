package ru.tbank.petcare.presentation.screen.createActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsNone
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.LabelText
import ru.tbank.petcare.presentation.mapper.toIndex
import ru.tbank.petcare.presentation.model.PetCardUIModel
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme

private const val HALF_ALPHA = 0.5f

@Composable
fun ActivitySegmentedControlButton(
    currentActivity: ActivityFormState,
    onSelected: (ActivityFormState) -> Unit
) {
    val trackHeight = 56.dp
    val shape = RoundedCornerShape(48.dp)

    val activities = ActivityFormState.list()
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .height(trackHeight),
            shape = shape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activities.forEachIndexed { index, activity ->
                    val isSelected = index == currentActivity.toIndex()

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onSelected(activity) },
                        shape = shape,
                        contentPadding = ButtonDefaults.ContentPadding,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                            contentColor = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            }
                        ),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val text = when (activity) {
                                is ActivityFormState.Walk -> stringResource(R.string.walk_quick_action_title)
                                is ActivityFormState.Grooming -> stringResource(R.string.grooming_quick_action_title)
                                is ActivityFormState.Vet -> stringResource(R.string.vet_quick_action_title)
                            }
                            Text(
                                text = text,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SetReminderCardSwitch(
    checked: Boolean = false,
    onCheckedChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.tertiary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = stringResource(R.string.notification_icon)
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.set_reminder_switch),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.get_notified_switch),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(HALF_ALPHA)

                )
            }
            Switch(
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    checkedThumbColor = MaterialTheme.colorScheme.background
                ),
                checked = checked,
                onCheckedChange = onCheckedChanged,
            )
        }
    }
}

@Composable
fun PetSelectCard(
    pets: List<PetCardUIModel>,
    selectPetId: String?,
    onPetSelected: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        LabelText(
            text = stringResource(R.string.select_pet_label)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pets, key = { it.id }) { pet ->
                val isSelected = pet.id == selectPetId
                PetAvatar(
                    pet,
                    isSelected = isSelected,
                    onClick = { onPetSelected(pet.id) }
                )
            }
            item {
                AddPetButton(
                    onClick = onAddClick
                )
            }
        }
    }
}

@Composable
fun PetAvatar(
    pet: PetCardUIModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.padding(3.dp)
                .size(64.dp)
                .border(
                    width = 2.dp,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.1f
                        )
                    },
                    shape = CircleShape
                ),

        ) {
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
        Text(
            text = pet.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            }
        )
    }
}

@Composable
private fun AddPetButton(onClick: () -> Unit,) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(3.dp)
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_pet),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Text(
            text = stringResource(R.string.add_pet),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Preview
@Composable
fun SwitchPreview() {
    PetCareTheme {
        SetReminderCardSwitch(
            checked = true,
            onCheckedChanged = {}
        )
    }
}

@Preview
@Composable
fun PreviewSegmentedControl() {
    PetCareTheme {
        ActivitySegmentedControlButton(
            currentActivity = ActivityFormState.Walk(),
            onSelected = {}
        )
    }
}
