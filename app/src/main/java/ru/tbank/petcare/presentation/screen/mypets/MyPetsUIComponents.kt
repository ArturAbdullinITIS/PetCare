package ru.tbank.petcare.presentation.screen.mypets

import android.graphics.Paint
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.utils.DateFormater
import ru.tbank.petcare.utils.getIconStatusUI

@Composable
fun MyPetsPetCard(
    pet: Pet,
    onPetClick: (String) -> Unit
) {
    val age = DateFormater.formatAgeYearsMonths(pet.dateOfBirth)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(80.dp)
        ) {

            Box {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(3.dp)
                ) {
                    AsyncImage(
                        model = pet.photoUrl,
                        contentDescription = stringResource(R.string.pets_photo),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        placeholder = painterResource(R.drawable.photo_placeholder),
                        contentScale = ContentScale.FillBounds,
                        error = painterResource(R.drawable.photo_placeholder)
                    )
                }
                IconStatusUI(
                    status = pet.iconStatus,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp)
                )
            }


            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,

            ) {
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pet.breed + " • " + age,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun IconStatusUI(
    status: IconStatus,
    modifier: Modifier = Modifier
) {
    val model = getIconStatusUI(status) ?: return

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.White)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(model.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = model.imageVector,
                contentDescription = stringResource(R.string.icon_status),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview
@Composable
fun MyPetsPetCardPreview() {
    MyPetsPetCard(
        pet = Pet(
            id = "1",
            name = "Cooper",
            breed = "Golden Retriever",
            photoUrl = "",
            dateOfBirth = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 3,
            iconStatus = IconStatus.NONE
        ),
        onPetClick = {}
    )
}

