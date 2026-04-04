package ru.tbank.petcare.presentation.screen.petProfile

import android.R.attr.fontWeight
import android.R.attr.text
import android.view.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter.State.Empty.painter
import org.w3c.dom.Text
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.IconStatusUI
import ru.tbank.petcare.presentation.common.ParameterCard
import ru.tbank.petcare.presentation.common.PetProfilePicture
import ru.tbank.petcare.presentation.model.PetForm

@Composable
fun PetProfileCard(
    pet: PetForm,
    onBreedClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(48.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(192.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.widthIn(max = 260.dp),
                            text = pet.name,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp,
                            lineHeight = 42.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        PublicPetIcon(isPublic = pet.isPublic)
                    }
                }
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(
                            onClick = {
                                onBreedClick()
                            }
                        ),
                    textAlign = TextAlign.Center,
                    text = pet.breed.uppercase(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ParameterCard(
                        parameterName = stringResource(R.string.age),
                        parameterValue = pet.dateOfBirthText
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ParameterCard(
                        parameterName = stringResource(R.string.gender_parameter),
                        parameterValue = pet.gender.name.lowercase()
                            .replaceFirstChar { ch ->
                                if (ch.isLowerCase()) ch.titlecase() else ch.toString()
                            }
                    )
                }

                ParameterCard(
                    parameterName = stringResource(R.string.weight),
                    parameterValue = "${pet.weight} kg"
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Box {
            PetProfilePicture(model = pet.photoUrl)
            IconStatusUI(
                status = pet.iconStatus,
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                iconSize = 46
            )
        }
    }
}

@Composable
fun PetProfileButton(
    text: String,
    icon: ImageVector,
    bg: Color,
    fg: Color,
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = fg
        ),
        onClick = onClick,
        modifier = Modifier.height(52.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.profile_button),
                tint = fg
            )
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = fg
            )
        }
    }
}

@Composable
fun PublicPetIcon(
    isPublic: Boolean = false
) {
    Surface(
        modifier = Modifier.size(28.dp),
        shape = CircleShape,
        color = if (isPublic) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.2f
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_public),
                contentDescription = stringResource(R.string.pet_is_public_icon),
                tint = if (isPublic) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.7f
                    )
                },
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
