package ru.tbank.petcare.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class PetIconStatusUIModel(
    val imageVector: ImageVector,
    val backgroundColor: Color,
    val iconTint: Color,
)