package ru.tbank.petcare.presentation.screen.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.mapper.toUIModel
import ru.tbank.petcare.presentation.ui.theme.WalkQuickActionIcon

@Composable
fun AnalyticsScreen(
    petId: String,
    onNavigateToRecent: (String) -> Unit
) {
    AnalyticsContent(
        petId = petId,
        onNavigateToRecent = onNavigateToRecent
    )
}

@Composable
private fun AnalyticsContent(
    modifier: Modifier = Modifier,
    petId: String,
    onNavigateToRecent: (String) -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel(
        key = petId,
        creationCallback = { factory: AnalyticsViewModel.Factory ->
            factory.create(petId = petId)
        }
    )
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PetShortInfo(
            pet = state.pet
        )
        AnalyticsPeriodSegmentedControl(
            currentPeriod = state.selectedPeriod,
            onSelected = {
                viewModel.processCommand(AnalyticsCommand.ChangePeriod(it))
            }
        )

        AnalyticsChartsPager(
            distanceChart = state.distanceChart,
            expensesChart = state.expensesChart
        )

        GoalCompletionCard(
            petName = state.pet.name,
            completedGoals = state.goalCompletion.completedGoals,
            totalGoals = state.goalCompletion.totalGoals,
            progress = state.goalCompletion.progress
        )

        StatCard(
            title = stringResource(R.string.total_walks).uppercase(),
            icon = {
                Icon(
                    imageVector = WalkQuickActionIcon,
                    contentDescription = stringResource(R.string.total_walks_icon),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            value = state.summary.totalWalks.toString(),
            bgColor = MaterialTheme.colorScheme.primaryContainer
        )
        StatCard(
            title = stringResource(R.string.avg_km).uppercase(),
            icon = {
                Icon(
                    painter = painterResource(R.drawable.avg_pace_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = stringResource(R.string.total_walks_icon),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            value = state.summary.avgKm.toString(),
            bgColor = MaterialTheme.colorScheme.secondaryContainer
        )
        StatCard(
            title = stringResource(R.string.total_expenses).uppercase(),
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_expenses),
                    contentDescription = stringResource(R.string.total_walks_icon),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            },
            value = state.summary.totalExpenses.toString(),
            bgColor = MaterialTheme.colorScheme.tertiaryContainer
        )
        ActivityHistoryTitle(
            onViewAllClick = {
                onNavigateToRecent(petId)
            }
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.lastActivities.forEach { activity ->
                ActivityHistoryCard(
                    model = activity.toUIModel()
                )
            }
        }
    }
}
