package ru.tbank.petcare.presentation.screen.publicProfiles

import BottomSheet
import android.R.attr.text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R

@Composable
fun PublicProfilesScreen(
    onPetClick: (String) -> Unit,
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
) {
    PublicProfilesContent(
        onPetClick = onPetClick,
        setTopBarActions = setTopBarActions
    )
}

@Suppress("MagicNumber")
@Composable
private fun PublicProfilesContent(
    onPetClick: (String) -> Unit,
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PublicProfilesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showSortSheet by rememberSaveable { mutableStateOf(false) }
    val isOnline by viewModel.isOnline.collectAsState()

    LaunchedEffect(Unit) {
        setTopBarActions {
            IconButton(onClick = { showSortSheet = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = stringResource(R.string.sort_icon)
                )
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { setTopBarActions(null) }
    }
    if (showSortSheet) {
        BottomSheet(
            onDismiss = { showSortSheet = false },
            selected = state.sortOption,
            onSelect = {
                viewModel.processCommand(PublicProfilesCommand.ChooseSortOption(it))
            },
        )
    }

    if (isOnline) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(40.dp)) }
            item {
                Text(
                    text = stringResource(R.string.community_feed).uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.6.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
            }
            item {
                Text(
                    text = stringResource(R.string.meet_the_pack),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            items(
                items = state.sortedPets,
                key = { it.id }
            ) { pet ->
                PublicPetCard(
                    pet = pet,
                    onClick = {
                        onPetClick(pet.id)
                    }
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.no_internet_please_check_your_connection_and_try_again),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
