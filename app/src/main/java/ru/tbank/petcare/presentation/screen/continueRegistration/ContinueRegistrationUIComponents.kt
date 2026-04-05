package ru.tbank.petcare.presentation.screen.continueRegistration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.EditPhotoIcon

@Composable
fun AddUserProfilePicture(
    onClick: () -> Unit,
    imageUrl: String
) {
    Column {
        Box {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                        .clickable(
                            onClick = onClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.pet_profile_picture),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(
                            CircleShape
                        )
                    )
                }
            }
            EditPhotoIcon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }
}
