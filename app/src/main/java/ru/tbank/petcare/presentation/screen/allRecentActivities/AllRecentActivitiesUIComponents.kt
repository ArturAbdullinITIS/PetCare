package ru.tbank.petcare.presentation.screen.allRecentActivities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.model.ActivityHistoryFilterOption

@Composable
fun FilterRow(
    modifier: Modifier = Modifier,
    selectedChip: ActivityHistoryFilterOption,
    onSelectChip: (ActivityHistoryFilterOption) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActivityFilterChip(
            selected = selectedChip == ActivityHistoryFilterOption.ALL,
            onClick = {
                onSelectChip(ActivityHistoryFilterOption.ALL)
            },
            label = stringResource(R.string.all)
        )
        ActivityFilterChip(
            selected = selectedChip == ActivityHistoryFilterOption.WALK,
            onClick = {
                onSelectChip(ActivityHistoryFilterOption.WALK)
            },
            label = stringResource(R.string.walk)
        )
        ActivityFilterChip(
            selected = selectedChip == ActivityHistoryFilterOption.GROOMING,
            onClick = {
                onSelectChip(ActivityHistoryFilterOption.GROOMING)
            },
            label = stringResource(R.string.grooming)
        )
        ActivityFilterChip(
            selected = selectedChip == ActivityHistoryFilterOption.VET,
            onClick = {
                onSelectChip(ActivityHistoryFilterOption.VET)
            },
            label = stringResource(R.string.veterinarian)
        )
    }
}

@Composable
fun ActivityFilterChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    FilterChip(
        modifier = modifier.padding(4.dp),
        selected = selected,
        onClick = onClick,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        label = {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        shape = RoundedCornerShape(32.dp)
    )
}
