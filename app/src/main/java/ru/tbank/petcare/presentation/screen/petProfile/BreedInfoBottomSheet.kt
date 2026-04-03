package ru.tbank.petcare.presentation.screen.petProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.PetInfo
import ru.tbank.petcare.presentation.common.CustomButton

private const val HALF_WEIGHT = 0.5f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedInfoBottomSheet(
    error: String? = null,
    petInfoUIModel: PetInfo? = null,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                petInfoUIModel != null -> {
                    Text(
                        text = stringResource(R.string.breed_information).uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = petInfoUIModel.breedName,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "\"${petInfoUIModel.slogan}\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BreedInfoCard(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            title = stringResource(R.string.group),
                            info = petInfoUIModel.group,
                            bg = MaterialTheme.colorScheme.primaryContainer,
                            fg = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        BreedInfoCard(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            title = stringResource(R.string.lifespan),
                            info = petInfoUIModel.lifespan,
                            bg = MaterialTheme.colorScheme.secondaryContainer,
                            fg = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BreedInfoCard(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            title = stringResource(R.string.diet),
                            info = petInfoUIModel.diet,
                            bg = MaterialTheme.colorScheme.tertiaryContainer,
                            fg = MaterialTheme.colorScheme.onTertiaryContainer

                        )
                        BreedInfoCard(
                            modifier = Modifier.weight(HALF_WEIGHT),
                            title = stringResource(R.string.skin_type),
                            info = petInfoUIModel.skinType,
                            bg = MaterialTheme.colorScheme.primaryContainer,
                            fg = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    BreedInfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.weight),
                        info = petInfoUIModel.weight,
                        bg = MaterialTheme.colorScheme.tertiaryContainer,
                        fg = MaterialTheme.colorScheme.onTertiaryContainer
                    )

                    val locationsText = petInfoUIModel.locations
                        .filter { it.isNotBlank() }
                        .joinToString(separator = ", ")

                    BreedInfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.locations),
                        info = locationsText.ifBlank { stringResource(R.string.unknown) },
                        bg = MaterialTheme.colorScheme.secondaryContainer,
                        fg = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                else -> {
                    Text(
                        text = stringResource(R.string.unknown),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss,
                text = stringResource(R.string.close),
                enabled = true
            )
        }
    }
}
