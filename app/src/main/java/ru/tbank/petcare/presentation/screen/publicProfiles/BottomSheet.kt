import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.model.PublicProfilesSortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    selected: PublicProfilesSortOption,
    onSelect: (PublicProfilesSortOption) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.choose_sorting_method),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(16.dp))

            SortRow(
                icon = Icons.Default.VideogameAsset,
                title = stringResource(R.string.game_score),
                isSelected = selected == PublicProfilesSortOption.GAME_SCORE,
                onClick = {
                    onSelect(PublicProfilesSortOption.GAME_SCORE)
                }
            )
            SortRow(
                icon = Icons.Default.SortByAlpha,
                title = stringResource(R.string.pet_name),
                isSelected = selected == PublicProfilesSortOption.NAME,
                onClick = {
                    onSelect(PublicProfilesSortOption.NAME)
                }
            )
            Spacer(Modifier.height(12.dp))
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss,
                text = stringResource(R.string.close),
                enabled = true
            )
        }
    }
}

@Composable
private fun SortRow(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.sort_option_icon)
        )
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 18.sp
        )
    }
}
