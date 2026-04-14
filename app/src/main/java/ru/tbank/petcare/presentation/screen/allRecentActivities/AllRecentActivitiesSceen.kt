package ru.tbank.petcare.presentation.screen.allRecentActivities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.presentation.mapper.toUIModel
import ru.tbank.petcare.presentation.screen.analytics.ActivityHistoryCard

@Composable
fun AllRecentActivitiesScreen(
    petId: String
) {
    AllRecentActivitiesContent(
        petId = petId
    )
}

@Composable
private fun AllRecentActivitiesContent(
    petId: String,
    modifier: Modifier = Modifier,
    viewModel: AllRecentActivitiesViewModel = hiltViewModel(
        key = petId,
        creationCallback = { factory: AllRecentActivitiesViewModel.Factory ->
            factory.create(petId)
        }
    )
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.activities) { activity ->
            ActivityHistoryCard(
                model = activity.toUIModel()
            )
        }
    }
}
