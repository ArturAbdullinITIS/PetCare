package ru.tbank.petcare.presentation.screen.mypets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
    onWalkClick: () -> Unit,
    onGroomingClick: () -> Unit,
    onVetClick: () -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    MyPetsContent(
        onWalkClick = onWalkClick,
        onGroomingClick = onGroomingClick,
        onVetClick = onVetClick,
        onNavigateToProfile = onNavigateToProfile
    )
}

private const val FRACTION = 0.6f

@Suppress("LongParameterList")
@Composable
private fun MyPetsContent(
    onNavigateToProfile: (String) -> Unit,
    onWalkClick: () -> Unit,
    onGroomingClick: () -> Unit,
    onVetClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPetsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pullState = rememberPullToRefreshState()
    val isOnline by viewModel.isOnline.collectAsState()

    PullToRefreshBox(
        state = pullState,
        onRefresh = { viewModel.refresh() },
        modifier = Modifier.fillMaxSize(),
        isRefreshing = state.isRefreshing
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                QuickActionsTitleRow()
            }

            item {
                QuickActionRow(
                    onWalkClick = onWalkClick,
                    onGroomingClick = onGroomingClick,
                    onVetClick = onVetClick,
                    enabled = isOnline
                )
            }

            item {
                if (state.isTipsLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    TipCard(
                        text = state.currentTip?.text ?: stringResource(R.string.no_tips_available),
                        onClick = { viewModel.nextTip() }
                    )
                }
            }

            item {
                YourFamilyTitle()
            }

            when {
                state.isPetsLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                state.pets.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight(FRACTION),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyPetsTitle(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                        }
                    }
                }

                else -> {
                    items(
                        items = state.pets,
                        key = { it.id }
                    ) { pet ->
                        MyPetsPetCard(
                            pet = pet,
                            onPetClick = { onNavigateToProfile(pet.id) },
                            clickable = isOnline
                        )
                    }
                }
            }
        }
    }
}
