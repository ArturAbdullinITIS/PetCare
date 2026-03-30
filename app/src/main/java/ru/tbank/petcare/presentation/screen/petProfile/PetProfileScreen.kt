package ru.tbank.petcare.presentation.screen.petProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.navigation.Route


@Composable
fun PetProfileScreen(petId: String, onNavigateToEdit: (String) -> Unit) {
    PetProfileContent(petId = petId, onNavigateToEdit = onNavigateToEdit)
}



@Composable
private fun PetProfileContent(
    onNavigateToEdit: (String) -> Unit,
    petId: String,
    modifier: Modifier = Modifier,
    viewModel: PetProfileViewModel = hiltViewModel(
        key = petId,
        creationCallback = { factory: PetProfileViewModel.Factory ->
            factory.create(petId = petId)
        }
    )
) {
    val state by viewModel.state.collectAsState()
    val pet = state.petProfileUIModel
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PetProfileCard(pet)
            NotesCard(pet.note)
            Spacer(modifier = Modifier.height(16.dp))
        }

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.create_activity_button),
            enabled = true
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PetProfileButton(
                text = stringResource(R.string.edit_profile),
                icon = Icons.Default.Edit,
                bg = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                fg = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    onNavigateToEdit(petId)
                }
            )
            PetProfileButton(
                text = "Analytics",
                icon = Icons.Default.Analytics,
                bg = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                fg = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = {}
            )
        }
    }
}