package ru.tbank.petcare.presentation.screen.createActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.common.CustomTextField
import ru.tbank.petcare.presentation.common.DobDatePickerDialog
import ru.tbank.petcare.presentation.model.GroomingProcedureType
import ru.tbank.petcare.presentation.model.VetProcedureType
import ru.tbank.petcare.utils.filterFloatInput
import java.util.Date

@Composable
fun CreateActivityScreen(
    petId: String?,
    type: String?,
    instanceId: String?,
    onAddClick: () -> Unit,
    onSaveActivityClick: () -> Unit
) {
    CreateActivityContent(
        petId = petId,
        type = type,
        instanceid = instanceId,
        onAddClick = onAddClick,
        onSaveActivityClick = onSaveActivityClick
    )
}

private const val HALF_WEIGHT = 0.5f

@Suppress("LongParameterList", "LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActivityContent(
    modifier: Modifier = Modifier,
    petId: String?,
    type: String?,
    instanceid: String?,
    viewModel: CreateActivityViewModel = hiltViewModel(
        key = instanceid,
        creationCallback = {factory: CreateActivityViewModel.Factory ->
            factory.create(petId,type, instanceid)
        }
    ),
    onAddClick: () -> Unit,
    onSaveActivityClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var showDobPicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CreateActivityEvent.Saved -> onSaveActivityClick()
                is CreateActivityEvent.Error -> {
                }
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
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
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PetSelectCard(
                pets = state.pets,
                selectPetId = state.selectedPetId,
                onPetSelected = { viewModel.processCommand(CreateActivityCommand.SelectPet(it)) },
                onAddClick = onAddClick
            )
            ActivitySegmentedControlButton(
                currentActivity = state.activityType,
                onSelected = {
                    viewModel.processCommand(
                        CreateActivityCommand.ChangeActivityType(
                            it
                        )
                    )
                }
            )
            CustomTextField(
                value = state.activityDateText,
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
                    initialMillisUtc = state.activityDate?.time ?: 0L,
                    onDismiss = { showDobPicker = false },
                    onConfirm = { millisUtc ->
                        showDobPicker = false
                        val pickedDate: Date? = if (millisUtc == 0L) null else Date(millisUtc)
                        viewModel.processCommand(
                            CreateActivityCommand.InputActivityDate(
                                pickedDate
                            )
                        )
                    }
                )
            }
            when (state.activityType) {
                is ActivityFormState.Walk -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomTextField(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            value = (state.activityType as ActivityFormState.Walk).form.goalKm,
                            onValueChange = {
                                viewModel.processCommand(
                                    CreateActivityCommand.InputGoalKm(
                                        filterFloatInput(it)
                                    )
                                )
                            },
                            maxLines = 1,
                            minLines = 1,
                            label = stringResource(R.string.goal_km),
                            placeholder = stringResource(R.string.goal_km_placeholder)
                        )
                        CustomTextField(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            value = (state.activityType as ActivityFormState.Walk).form.actualKm,
                            onValueChange = {
                                viewModel.processCommand(
                                    CreateActivityCommand.InputActualKm(
                                        filterFloatInput(it)
                                    )
                                )
                            },
                            maxLines = 1,
                            minLines = 1,
                            label = stringResource(R.string.actual_km),
                            placeholder = stringResource(R.string.actual_km_placeholder)
                        )
                    }
                }

                is ActivityFormState.Grooming -> {
                    val form = (state.activityType as ActivityFormState.Grooming).form
                    var expandedGrooming by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expandedGrooming,
                        onExpandedChange = { expandedGrooming = !expandedGrooming }
                    ) {
                        CustomTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(type = PrimaryNotEditable),
                            value = form.procedureType.value,
                            onValueChange = {},
                            readOnly = true,
                            maxLines = 1,
                            label = stringResource(R.string.procedure_type),
                            placeholder = stringResource(R.string.procedure_type),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedGrooming
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            expanded = expandedGrooming,
                            onDismissRequest = { expandedGrooming = false }
                        ) {
                            GroomingProcedureType.entries.forEach { procedure ->
                                DropdownMenuItem(
                                    text = { Text(procedure.value) },
                                    onClick = {
                                        viewModel.processCommand(
                                            CreateActivityCommand.InputGroomingProcedure(procedure)
                                        )
                                        expandedGrooming = false
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomTextField(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            value = form.groomingCost,
                            onValueChange = {
                                viewModel.processCommand(CreateActivityCommand.InputGroomingCost(filterFloatInput(it)))
                            },
                            maxLines = 1,
                            minLines = 1,
                            label = "Cost",
                            placeholder = "0.0"
                        )
                        CustomTextField(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            value = form.durationMinutes,
                            onValueChange = {
                                viewModel.processCommand(
                                    CreateActivityCommand.InputGroomingDuration(filterFloatInput(it))
                                )
                            },
                            maxLines = 1,
                            minLines = 1,
                            label = stringResource(R.string.duration_label),
                            placeholder = stringResource(R.string.duration_placeholder)
                        )
                    }
                }

                is ActivityFormState.Vet -> {
                    val form = (state.activityType as ActivityFormState.Vet).form
                    var expandedVet by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expandedVet,
                        onExpandedChange = { expandedVet = !expandedVet }
                    ) {
                        CustomTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(type = PrimaryNotEditable),
                            value = form.procedureType.value,
                            onValueChange = {},
                            readOnly = true,
                            maxLines = 1,
                            label = stringResource(R.string.procedure_type_label),
                            placeholder = stringResource(R.string.select_procedure_placeholder),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedVet
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            expanded = expandedVet,
                            onDismissRequest = { expandedVet = false }
                        ) {
                            VetProcedureType.entries.forEach { procedure ->
                                DropdownMenuItem(
                                    text = { Text(procedure.value) },
                                    onClick = {
                                        viewModel.processCommand(CreateActivityCommand.InputVetProcedure(procedure))
                                        expandedVet = false
                                    }
                                )
                            }
                        }
                    }
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = form.vetCost,
                        onValueChange = {
                            viewModel.processCommand(CreateActivityCommand.InputVetCost(filterFloatInput(it)))
                        },
                        maxLines = 1,
                        minLines = 1,
                        label = "Cost",
                        placeholder = "0.0"
                    )
                }
            }
            CustomTextField(
                modifier = modifier.fillMaxWidth(),
                value = state.activityNotes,
                onValueChange = { viewModel.processCommand(CreateActivityCommand.InputNotes(it)) },
                label = stringResource(R.string.activity_notes),
                maxLines = 4,
                minLines = 4,
                placeholder = stringResource(R.string.activity_notes)
            )
            SetReminderCardSwitch(
                checked = state.isReminder,
                onCheckedChanged = { viewModel.processCommand(CreateActivityCommand.IsReminder) }
            )
            CustomButton(
                modifier = modifier.fillMaxWidth(),
                onClick = { viewModel.processCommand(CreateActivityCommand.SaveActivity) },
                text = stringResource(R.string.save_activity),
                enabled = state.isButtonEnabled
            )
        }
    }
}
