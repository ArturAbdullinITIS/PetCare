package ru.tbank.petcare.presentation.screen.petProfile

import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State.Empty.painter
import org.w3c.dom.Text
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.presentation.common.IconStatusUI
import ru.tbank.petcare.presentation.model.PetForm
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme
import ru.tbank.petcare.utils.DateFormater
import java.util.Locale

@Composable
fun PetProfileCard(pet: PetForm) {
    val pictureSize = 192.dp
    val overlap = 8.dp
    val topPadding = (pictureSize / 5) - overlap
    val contentTopSpacer = pictureSize - overlap * 2

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = overlap),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding),
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
                Spacer(modifier = Modifier.height(contentTopSpacer))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = pet.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 36.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    PublicPetIcon(isPublic = pet.isPublic)
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = pet.breed.uppercase(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
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
                        parameterValue = DateFormater.formatAgeYearsMonths(pet.dateOfBirth)
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
                    .align(Alignment.BottomEnd)
                    .size(40.dp),
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
fun PetProfilePicture(
    model: String = ""
) {
    val shape = RoundedCornerShape(48.dp)

    Surface(
        modifier = Modifier.size(192.dp),
        shape = shape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(shape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = model,
                contentDescription = stringResource(R.string.pet_profile_pic),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.photo_placeholder),
                error = painterResource(R.drawable.photo_placeholder)
            )
        }
    }
}

@Composable
fun ParameterCard(
    parameterName: String,
    parameterValue: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$parameterName: ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = parameterValue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
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
        color = if(isPublic) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_public),
                contentDescription = stringResource(R.string.pet_is_public_icon),
                tint = if(isPublic)MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


@Composable
fun NotesCard(
    note: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(0.5f),
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        shape = RoundedCornerShape(48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.features_notes_card).uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp
            )
            Text(
                text = note,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 4
            )
        }
    }
}

@Composable
@Preview
fun ParameterCardPreview() {
    PetCareTheme {
        ParameterCard(
            parameterName = "Age",
            parameterValue = "2 years"
        )
    }
}


@Composable
@Preview
fun PetCardPreview() {
    PetCareTheme {
        PetProfileCard(
            pet = PetForm(
                name = "Cooper",
                breed = "Golden Retriever",
                photoUrl = "",
                iconStatus = IconStatus.STAR,
                weight = 25.4.toString()
            )
        )
    }
}