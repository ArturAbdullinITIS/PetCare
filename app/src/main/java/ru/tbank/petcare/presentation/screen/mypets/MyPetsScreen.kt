package ru.tbank.petcare.presentation.screen.mypets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R

@Composable
fun MyPetsScreen(
    onNavigateToProfile: (String) -> Unit
) {
    MyPetsContent(
        onNavigateToProfile = onNavigateToProfile
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyPetsContent(
    onNavigateToProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPetsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuickActionsTitleRow()
        QuickActionRow(
            onWalkClick = {},
            onGroomingClick = {},
            onVetClick = {}
        )
        if (state.isTipsLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            TipCard(
                text = state.currentTip?.text ?: stringResource(R.string.no_tips_available),
                onClick = { viewModel.nextTip() }
            )
        }
        YourFamilyTitle()
        when {
            state.isPetsLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.pets.isEmpty() -> {
                EmptyPetsTitle(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = state.pets,
                        key = { it.id }
                    ) { pet ->
                        MyPetsPetCard(
                            pet = pet,
                            onPetClick = {
                                onNavigateToProfile(pet.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
