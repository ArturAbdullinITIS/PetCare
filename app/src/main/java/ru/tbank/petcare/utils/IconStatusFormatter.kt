package ru.tbank.petcare.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.presentation.model.PetIconStatusUIModel
import ru.tbank.petcare.presentation.ui.theme.BoltIconStatus
import ru.tbank.petcare.presentation.ui.theme.FlameIconStatus
import ru.tbank.petcare.presentation.ui.theme.HeartIconStatus
import ru.tbank.petcare.presentation.ui.theme.MoonIconStatus
import ru.tbank.petcare.presentation.ui.theme.StarIconStatus
import ru.tbank.petcare.presentation.ui.theme.SunIconStatus

fun getIconStatusUI(iconStatus: IconStatus): PetIconStatusUIModel? {
    return when(iconStatus) {
        IconStatus.STAR -> PetIconStatusUIModel(
            imageVector = StarIconStatus,
            backgroundColor = Color.Cyan
        )
        IconStatus.HEART -> PetIconStatusUIModel(
            imageVector = HeartIconStatus,
            backgroundColor = Color.Red
        )
        IconStatus.SUN -> PetIconStatusUIModel(
            imageVector = SunIconStatus,
            backgroundColor = Color.Yellow
        )
        IconStatus.MOON -> PetIconStatusUIModel(
            imageVector = MoonIconStatus,
            backgroundColor = Color.Magenta
        )
        IconStatus.FLAME -> PetIconStatusUIModel(
            imageVector = FlameIconStatus,
            backgroundColor = Color.Yellow
        )
        IconStatus.BOLT -> PetIconStatusUIModel(
            imageVector = BoltIconStatus,
            backgroundColor = Color.Yellow
        )
        IconStatus.NONE -> null
    }
}