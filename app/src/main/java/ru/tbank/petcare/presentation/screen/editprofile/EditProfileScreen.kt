package ru.tbank.petcare.presentation.screen.editprofile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ru.tbank.petcare.presentation.common.AddPetProfilePicture
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.common.CustomTextField

@Composable
fun EditProfileScreen(
    onContinue: () -> Unit
) {
    EditProfileContent(
        onContinue = onContinue
    )
}

@Composable
fun EditProfileContent(
    onContinue: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.processCommand(EditProfileCommand.SetPhoto(uri))
            }
        }
    )
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is EditProfileEvent.Error -> {
                }
                EditProfileEvent.Saved -> {
                    onContinue()
                }
                EditProfileEvent.LaunchImagePicker -> {
                    imagePicker.launch("image/*")
                }
            }
        }
    }
    Card(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
            .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddPetProfilePicture(
                onClick = {
                    viewModel.processCommand(EditProfileCommand.PickImage)
                },
                imageUrl = if (state.selectedPhotoUri == null) {
                    state.user.photoUrl
                } else {
                    state.selectedPhotoUri.toString()
                }
            )
            Text(
                text = stringResource(R.string.edit_profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.you_can_edit_your_profile),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
            CustomTextField(
                value = state.user.firstName,
                onValueChange = {
                    viewModel.processCommand(EditProfileCommand.InputFirstName(it))
                },
                placeholder = stringResource(R.string.your_first_name),
                label = stringResource(R.string.first_name).uppercase(),
                maxLines = 1,
                minLines = 1,
                singleLine = true,
                readOnly = false,
            )
            CustomTextField(
                value = state.user.lastName,
                onValueChange = {
                    viewModel.processCommand(EditProfileCommand.InputLastName(it))
                },
                placeholder = stringResource(R.string.your_last_name),
                label = stringResource(R.string.last_name).uppercase(),
                maxLines = 1,
                minLines = 1,
                singleLine = true,
                readOnly = false,
            )
            HorizontalDivider(modifier = Modifier.padding(8.dp))
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.processCommand(EditProfileCommand.Edit)
                },
                content = {
                    if (state.isEditing) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.arrow_forward),
                        )
                    }
                },
                text = if (state.isEditing) null else stringResource(R.string.edit_profile),
                enabled = state.isEnabled,
            )
        }
    }
}