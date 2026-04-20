package ru.tbank.petcare.presentation.screen.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.mapper.toOnboardingPageUIModel

@Composable
fun OnboardingScreen(
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
    onFinished: () -> Unit
) {
    OnboardingContent(
        setTopBarActions = setTopBarActions,
        onFinished = onFinished
    )
}

@Composable
private fun OnboardingContent(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
    onFinished: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        setTopBarActions {
            TextButton(
                onClick = {
                    viewModel.processCommand(OnboardingCommand.Finish)
                }
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.totalPages }
    )

    LaunchedEffect(state.currentPage) {
        pagerState.animateScrollToPage(state.currentPage)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                OnboardingEvents.Finished -> onFinished()
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.processCommand(OnboardingCommand.Reset)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) { page ->
                val model = toOnboardingPageUIModel(page)

                OnboardingPageContent(
                    model = model,
                    content = {
                        when (page) {
                            0 -> WelcomeContentCard()
                            1 -> TrackCard()
                            2 -> ConnectCard()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OnboardingPagerIndicator(
                currentPage = state.currentPage,
                pageCount = state.totalPages
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (state.currentPage == state.totalPages - 1) {
                            viewModel.processCommand(OnboardingCommand.Finish)
                        } else {
                            viewModel.processCommand(
                                OnboardingCommand.ChangePage(state.currentPage + 1)
                            )
                        }
                    },
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.arrow_forward)
                        )
                    },
                    text = stringResource(state.currentPageUI.buttonTextRes),
                    enabled = true
                )

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrow_forward)
                        )
                    },
                    onClick = {
                        viewModel.processCommand(
                            OnboardingCommand.ChangePage(state.currentPage - 1)
                        )
                    },
                    text = stringResource(R.string.back),
                    enabled = state.currentPage > 0
                )
            }
        }
    }
}
