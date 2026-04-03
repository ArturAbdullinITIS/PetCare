package ru.tbank.petcare.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.presentation.mapper.toIndex
import ru.tbank.petcare.presentation.ui.theme.HeartIconStatus
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme
import ru.tbank.petcare.presentation.ui.theme.SparklesIconStatus
import ru.tbank.petcare.presentation.ui.theme.StarIconStatus
import java.util.Locale

@Composable
fun PublicProfileCardSwitch(
    checked: Boolean = false,
    onCheckedChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    painter = painterResource(R.drawable.ic_public),
                    contentDescription = stringResource(R.string.public_profile_switch),
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.public_profile),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.visible_to_local_pet_owners),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
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
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String,
    maxLines: Int,
    minLines: Int = 1,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyBoardOption: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LabelText(
            text = label
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Normal
                )
            },
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            maxLines = maxLines,
            singleLine = singleLine,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            keyboardOptions = keyBoardOption,
            minLines = minLines
        )
    }
}

@Composable
fun LabelText(
    text: String
) {
    Text(
        text = text.uppercase(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        letterSpacing = 1.1.sp
    )
}

@Composable
fun SelectableIconStatus(
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
        } else {
            null
        },
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.1f
            )
        } else {
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.1f
            )
        },
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    )
                }
            )
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DobDatePickerDialog(
    initialMillisUtc: Long,
    onDismiss: () -> Unit,
    onConfirm: (millisUtc: Long) -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialMillisUtc.takeIf { it != 0L },
        selectableDates = object : androidx.compose.material3.SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = state.selectedDateMillis != null,
                onClick = {
                    val selected = state.selectedDateMillis ?: return@TextButton
                    onConfirm(selected)
                }
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = state)
    }
}

@Composable
fun SelectableIconStatusRow(
    currentIconsStatus: IconStatus,
    onSelectIconStatus: (IconStatus) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),

    ) {
        LabelText(
            text = stringResource(R.string.select_icon)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconStatus.entries.forEach { status ->
                val isSelected = status == currentIconsStatus
                SelectableIconStatus(
                    content = {
                        Icon(
                            imageVector = when (status) {
                                IconStatus.HEART -> HeartIconStatus
                                IconStatus.SPARKLES -> SparklesIconStatus
                                IconStatus.STAR -> StarIconStatus
                                IconStatus.NONE -> Icons.Default.Clear
                            },
                            contentDescription = stringResource(
                                R.string.icon_status_row,
                                status.name
                            )
                        )
                    },
                    onClick = {
                        onSelectIconStatus(status)
                    },
                    isSelected = isSelected
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun CustomSegmentedControlButton(
    currentGender: Gender,
    onSelected: (Gender) -> Unit
) {
    val trackHeight = 56.dp
    val shape = RoundedCornerShape(48.dp)

    val genders = Gender.entries.filterNot { it == Gender.UNKNOWN }

    Column {
        LabelText(
            text = stringResource(R.string.gender)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight),
            shape = shape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                genders.forEachIndexed { index, gender ->
                    val isSelected = index == currentGender.toIndex()

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onSelected(gender) },
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
                            val icon = when (gender) {
                                Gender.MALE -> Icons.Default.Male
                                Gender.FEMALE -> Icons.Default.Female
                                Gender.UNKNOWN -> null
                            }

                            if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = stringResource(R.string.gender_icon),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            val title = gender.name.lowercase(Locale.getDefault())
                                .replaceFirstChar { ch ->
                                    if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()) else ch.toString()
                                }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddPetProfilePicture(
    onClick: () -> Unit,
    imageUrl: String
) {
    Column {
        Box {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                        .clickable(
                            onClick = onClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.pet_profile_picture),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(
                            CircleShape
                        )
                    )
                }
            }
            EditPhotoIcon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun EditPhotoIcon(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.icon_status),
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun AddPetProfilePicturePreview() {
    PetCareTheme {
        AddPetProfilePicture(
            onClick = {
            },
            imageUrl = ""
        )
    }
}

@Preview
@Composable
fun SelectableIconStatusRowPreview() {
    PetCareTheme {
        SelectableIconStatusRow(
            currentIconsStatus = IconStatus.HEART,
            onSelectIconStatus = {}
        )
    }
}

@Preview
@Composable
fun PublicProfileCardPreview() {
    PetCareTheme {
        PublicProfileCardSwitch(
            onCheckedChanged = {}
        )
    }
}

@Composable
@Preview
fun SelectableIconStatusPreview() {
    PetCareTheme {
        SelectableIconStatus(
            content = {
                Icon(
                    imageVector = HeartIconStatus,
                    contentDescription = null
                )
            },
            onClick = {},
            isSelected = true
        )
    }
}

@Preview
@Composable
fun CustomTextFieldPreview() {
    PetCareTheme {
        CustomTextField(
            value = "",
            onValueChange = {},
            placeholder = "Enter pet name",
            label = "Pet Name",
            maxLines = 1,
            minLines = 1
        )
    }
}
