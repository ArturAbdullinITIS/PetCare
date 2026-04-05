package ru.tbank.petcare.presentation.screen.continueRegistration

import android.R.attr.enabled
import android.graphics.drawable.Icon
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
import androidx.compose.material.icons.filled.ArrowForward
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
fun ContinueRegistrationScreen(
    onContinue: () -> Unit,
) {
    ContinueRegistrationContent(onContinue = onContinue)
}

@Composable
fun ContinueRegistrationContent(
    onContinue: () -> Unit,
    viewModel: ContinueRegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.processCommand(ContinueRegistrationCommand.SetPhoto(uri))
            }
        }
    )
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ContinueRegistrationEvent.Error -> {
                }
                ContinueRegistrationEvent.Saved -> {
                    onContinue()
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
                    imagePicker.launch(
                        "image/*"
                    )
                },
                imageUrl = state.selectedPhotoUri.toString()
            )
            Text(
                text = stringResource(R.string.welcome),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.let_s_finish_setting_up_your_account),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
            CustomTextField(
                value = state.user.firstName,
                onValueChange = {
                    viewModel.processCommand(ContinueRegistrationCommand.InputFirstName(it))
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
                    viewModel.processCommand(ContinueRegistrationCommand.InputLastName(it))
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
                    viewModel.processCommand(ContinueRegistrationCommand.Save)
                },
                content = {
                    if (state.isSaving) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.arrow_forward),
                        )
                    }
                },
                text = if (state.isSaving) null else stringResource(R.string.get_started),
                enabled = state.isButtonEnabled,
            )
        }
    }
}
