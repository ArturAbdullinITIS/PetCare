package ru.tbank.petcare.presentation.screen.addpet

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.AddPetProfilePicture
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.common.CustomSegmentedControlButton
import ru.tbank.petcare.presentation.common.CustomTextField
import ru.tbank.petcare.presentation.common.DobDatePickerDialog
import ru.tbank.petcare.presentation.common.PublicProfileCardSwitch
import ru.tbank.petcare.presentation.common.SelectableIconStatusRow
import ru.tbank.petcare.utils.DateFormater


@Composable
fun AddPetScreen(
    onAddClick: () -> Unit
) {
    AddPetContent(onAddClick)
}

@Composable
private fun AddPetContent(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var showDobPicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.processCommand(AddPetCommand.AddPhotoUrl(it.toString()))
            }
        }
    )

    Card(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddPetProfilePicture(
                onClick = {
                    imagePicker.launch(
                        "image/*"
                    )
                },
                imageUrl = state.petUIModel.photoUrl
            )
            Text(
                text = stringResource(R.string.add_profile_photo),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            SelectableIconStatusRow(
                currentIconsStatus = state.petUIModel.iconStatus,
                onSelectIconStatus = {
                    viewModel.processCommand(AddPetCommand.ChangeIconStatus(it))
                }
            )
            CustomTextField(
                value = state.petUIModel.name,
                onValueChange = {
                    viewModel.processCommand(AddPetCommand.InputName(it))
                },
                placeholder = stringResource(R.string.name_pet_placeholder),
                label = stringResource(R.string.pet_name),
                maxLines = 1,
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    modifier = Modifier.weight(0.7f),
                    value = state.petUIModel.breed,
                    onValueChange = {
                        viewModel.processCommand(AddPetCommand.InputBreed(it))
                    },
                    placeholder = stringResource(R.string.breed_pet_placeholder),
                    label = stringResource(R.string.breed),
                    maxLines = 1,
                    singleLine = true
                )
                CustomTextField(
                    modifier = Modifier.weight(0.3f),
                    value = state.petUIModel.weight,
                    onValueChange = { text ->
                            viewModel.processCommand(AddPetCommand.InputWeight(filterWeightInput(text)))

                    },
                    placeholder = stringResource(R.string.weight_pet_placeholder),
                    label = stringResource(R.string.weight_kg),
                    maxLines = 1,
                    singleLine = true,
                    keyBoardOption = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
            }
            CustomTextField(
                value = DateFormater.formatDob(state.petUIModel.dateOfBirth),
                onValueChange = { },
                placeholder = "dd.mm.yyyy",
                label = stringResource(R.string.date_of_birth),
                maxLines = 1,
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = stringResource(R.string.calendar_icon),
                        modifier = Modifier.clickable {
                            focusManager.clearFocus()
                            showDobPicker = true
                        }
                    )
                }
            )
            if (showDobPicker) {
                DobDatePickerDialog(
                    initialMillisUtc = state.petUIModel.dateOfBirth,
                    onDismiss = { showDobPicker = false },
                    onConfirm = { millisUtc ->
                        showDobPicker = false
                        viewModel.processCommand(AddPetCommand.InputDateOfBirth(millisUtc))
                    }
                )
            }
            CustomSegmentedControlButton(
                currentGender = state.petUIModel.gender,
                onSelected = {
                    viewModel.processCommand(AddPetCommand.ChangeGender(it))
                }
            )
            CustomTextField(
                value = state.petUIModel.note,
                onValueChange = {
                    viewModel.processCommand(AddPetCommand.InputNotes(it))
                },
                placeholder = stringResource(R.string.note_pet_placeholder),
                label = stringResource(R.string.features_notes),
                maxLines = 4,
                minLines = 4
            )
            PublicProfileCardSwitch(
                checked = state.petUIModel.isPublic,
                onCheckedChanged = {
                    viewModel.processCommand(AddPetCommand.IsPublic)
                }
            )
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isButtonEnabled,
                onClick = {
                    viewModel.processCommand(AddPetCommand.AddPet)
                    onAddClick()
                },
                content = {},
                text = stringResource(R.string.add_pet)
            )
        }
    }
}

private fun filterWeightInput(raw: String, maxLen: Int = 5): String {
    val normalized = raw.replace(',', '.')

    val filtered = buildString {
        var dotUsed = false
        for (ch in normalized) {
            when {
                ch.isDigit() -> append(ch)
                ch == '.' && !dotUsed -> {
                    if (isEmpty()) continue
                    append('.')
                    dotUsed = true
                }
            }
        }
    }

    return filtered.take(maxLen)
}