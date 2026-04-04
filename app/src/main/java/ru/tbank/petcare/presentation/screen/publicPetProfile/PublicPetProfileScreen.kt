package ru.tbank.petcare.presentation.screen.publicPetProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.presentation.common.NotesCard
import ru.tbank.petcare.presentation.screen.petProfile.BreedInfoBottomSheet

@Composable
fun PublicPetProfileScreen(petId: String) {
    PublicPetProfileContent(petId = petId)
}

@Composable
private fun PublicPetProfileContent(
    petId: String,
    modifier: Modifier = Modifier,
    viewModel: PublicPetProfileViewModel = hiltViewModel(
        key = petId,
        creationCallback = { factory: PublicPetProfileViewModel.Factory ->
            factory.create(petId = petId)
        }
    )
) {
    val state by viewModel.state.collectAsState()
    val pet = state.petProfileUIModel
    val petInfo = state.petInfoUIModel
    val scrollState = rememberScrollState()
    var showBreedSheet by remember { mutableStateOf(false) }

    if (showBreedSheet) {
        BreedInfoBottomSheet(
            petInfoUIModel = petInfo,
            error = state.errorMessage,
            onDismiss = { showBreedSheet = false },
            isLoading = state.isInfoLoading
        )
    }

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
            PublicPetProfileCard(
                pet = pet,
                onBreedClick = {
                    showBreedSheet = true
                    viewModel.processCommand(PublicPetProfileCommand.ShowPetInfo(pet.breed))
                }
            )
            OwnerCard(pet.ownerName)
            GameScoreCard(pet.gameScoreText)
            NotesCard(pet.note)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
