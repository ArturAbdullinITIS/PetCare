package ru.tbank.petcare.presentation.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class QuickActionUIModel(
    @StringRes val title: Int,
    val icon: ImageVector,
    val containerColor: Color,
    val contentColor: Color
)

enum class QuickActionType() {
    WALK, VET, GROOMING
}
