package ru.tbank.petcare.presentation.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SunIconStatus: ImageVector
    get() {
        if (_FontAwesomeSun != null) return _FontAwesomeSun!!

        _FontAwesomeSun = ImageVector.Builder(
            name = "sun",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(256f, 160f)
                curveToRelative(-52.9f, 0f, -96f, 43.1f, -96f, 96f)
                reflectiveCurveToRelative(43.1f, 96f, 96f, 96f)
                reflectiveCurveToRelative(96f, -43.1f, 96f, -96f)
                reflectiveCurveToRelative(-43.1f, -96f, -96f, -96f)
                close()
                moveToRelative(246.4f, 80.5f)
                lineToRelative(-94.7f, -47.3f)
                lineToRelative(33.5f, -100.4f)
                curveToRelative(4.5f, -13.6f, -8.4f, -26.5f, -21.9f, -21.9f)
                lineToRelative(-100.4f, 33.5f)
                lineToRelative(-47.4f, -94.8f)
                curveToRelative(-6.4f, -12.8f, -24.6f, -12.8f, -31f, 0f)
                lineToRelative(-47.3f, 94.7f)
                lineTo(92.7f, 70.8f)
                curveToRelative(-13.6f, -4.5f, -26.5f, 8.4f, -21.9f, 21.9f)
                lineToRelative(33.5f, 100.4f)
                lineToRelative(-94.7f, 47.4f)
                curveToRelative(-12.8f, 6.4f, -12.8f, 24.6f, 0f, 31f)
                lineToRelative(94.7f, 47.3f)
                lineToRelative(-33.5f, 100.5f)
                curveToRelative(-4.5f, 13.6f, 8.4f, 26.5f, 21.9f, 21.9f)
                lineToRelative(100.4f, -33.5f)
                lineToRelative(47.3f, 94.7f)
                curveToRelative(6.4f, 12.8f, 24.6f, 12.8f, 31f, 0f)
                lineToRelative(47.3f, -94.7f)
                lineToRelative(100.4f, 33.5f)
                curveToRelative(13.6f, 4.5f, 26.5f, -8.4f, 21.9f, -21.9f)
                lineToRelative(-33.5f, -100.4f)
                lineToRelative(94.7f, -47.3f)
                curveToRelative(13f, -6.5f, 13f, -24.7f, 0.2f, -31.1f)
                close()
                moveToRelative(-155.9f, 106f)
                curveToRelative(-49.9f, 49.9f, -131.1f, 49.9f, -181f, 0f)
                curveToRelative(-49.9f, -49.9f, -49.9f, -131.1f, 0f, -181f)
                curveToRelative(49.9f, -49.9f, 131.1f, -49.9f, 181f, 0f)
                curveToRelative(49.9f, 49.9f, 49.9f, 131.1f, 0f, 181f)
                close()
            }
        }.build()

        return _FontAwesomeSun!!
    }

private var _FontAwesomeSun: ImageVector? = null

val HeartIconStatus: ImageVector
    get() {
        if (_FontAwesomeHeart != null) return _FontAwesomeHeart!!

        _FontAwesomeHeart = ImageVector.Builder(
            name = "heart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(462.3f, 62.6f)
                curveTo(407.5f, 15.9f, 326f, 24.3f, 275.7f, 76.2f)
                lineTo(256f, 96.5f)
                lineToRelative(-19.7f, -20.3f)
                curveTo(186.1f, 24.3f, 104.5f, 15.9f, 49.7f, 62.6f)
                curveToRelative(-62.8f, 53.6f, -66.1f, 149.8f, -9.9f, 207.9f)
                lineToRelative(193.5f, 199.8f)
                curveToRelative(12.5f, 12.9f, 32.8f, 12.9f, 45.3f, 0f)
                lineToRelative(193.5f, -199.8f)
                curveToRelative(56.3f, -58.1f, 53f, -154.3f, -9.8f, -207.9f)
                close()
            }
        }.build()

        return _FontAwesomeHeart!!
    }

private var _FontAwesomeHeart: ImageVector? = null


val StarIconStatus: ImageVector
    get() {
        if (_RadixStarFilled != null) return _RadixStarFilled!!

        _RadixStarFilled = ImageVector.Builder(
            name = "star-filled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 15f,
            viewportHeight = 15f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(7.22257f, 0.665927f)
                curveTo(7.32508f, 0.419634f, 7.67476f, 0.419617f, 7.77726f, 0.665927f)
                lineTo(9.413f, 4.6005f)
                curveTo(9.45615f, 4.70425f, 9.55396f, 4.77497f, 9.66593f, 4.78409f)
                lineTo(13.914f, 5.12491f)
                curveTo(14.1799f, 5.14635f, 14.2875f, 5.47869f, 14.0849f, 5.65226f)
                lineTo(10.8485f, 8.42374f)
                curveTo(10.7632f, 8.49693f, 10.7258f, 8.61221f, 10.7519f, 8.72159f)
                lineTo(11.7411f, 12.8661f)
                curveTo(11.803f, 13.1256f, 11.5206f, 13.331f, 11.2929f, 13.1923f)
                lineTo(7.65616f, 10.9706f)
                curveTo(7.56022f, 10.9121f, 7.43961f, 10.9121f, 7.34366f, 10.9706f)
                lineTo(3.70694f, 13.1923f)
                curveTo(3.47926f, 13.3311f, 3.19681f, 13.1256f, 3.2587f, 12.8661f)
                lineTo(4.24796f, 8.72159f)
                curveTo(4.27405f, 8.61223f, 4.23661f, 8.49693f, 4.15128f, 8.42374f)
                lineTo(0.914951f, 5.65226f)
                curveTo(0.712311f, 5.47867f, 0.819914f, 5.1463f, 1.08585f, 5.12491f)
                lineTo(5.3339f, 4.78409f)
                curveTo(5.44584f, 4.77494f, 5.54368f, 4.70422f, 5.58683f, 4.6005f)
                lineTo(7.22257f, 0.665927f)
                close()
            }
        }.build()

        return _RadixStarFilled!!
    }

private var _RadixStarFilled: ImageVector? = null


val MoonIconStatus: ImageVector
    get() {
        if (_FontAwesomeMoon != null) return _FontAwesomeMoon!!

        _FontAwesomeMoon = ImageVector.Builder(
            name = "moon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(283.211f, 512f)
                curveToRelative(78.962f, 0f, 151.079f, -35.925f, 198.857f, -94.792f)
                curveToRelative(7.068f, -8.708f, -0.639f, -21.43f, -11.562f, -19.35f)
                curveToRelative(-124.203f, 23.654f, -238.262f, -71.576f, -238.262f, -196.954f)
                curveToRelative(0f, -72.222f, 38.662f, -138.635f, 101.498f, -174.394f)
                curveToRelative(9.686f, -5.512f, 7.25f, -20.197f, -3.756f, -22.23f)
                arcTo(258.156f, 258.156f, 0f, false, false, 283.211f, 0f)
                curveToRelative(-141.309f, 0f, -256f, 114.511f, -256f, 256f)
                curveToRelative(0f, 141.309f, 114.511f, 256f, 256f, 256f)
                close()
            }
        }.build()

        return _FontAwesomeMoon!!
    }

private var _FontAwesomeMoon: ImageVector? = null

val FlameIconStatus: ImageVector
    get() {
        if (_LucideFlame != null) return _LucideFlame!!

        _LucideFlame = ImageVector.Builder(
            name = "flame",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 3f)
                quadToRelative(1f, 4f, 4f, 6.5f)
                reflectiveQuadToRelative(3f, 5.5f)
                arcToRelative(1f, 1f, 0f, false, true, -14f, 0f)
                arcToRelative(5f, 5f, 0f, false, true, 1f, -3f)
                arcToRelative(1f, 1f, 0f, false, false, 5f, 0f)
                curveToRelative(0f, -2f, -1.5f, -3f, -1.5f, -5f)
                quadToRelative(0f, -2f, 2.5f, -4f)
            }
        }.build()

        return _LucideFlame!!
    }

private var _LucideFlame: ImageVector? = null


val BoltIconStatus: ImageVector
    get() {
        if (_MaterialIconsBolt != null) return _MaterialIconsBolt!!

        _MaterialIconsBolt = ImageVector.Builder(
            name = "bolt",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            group {
                path(
                    fill = SolidColor(Color.Transparent)
                ) {
                    moveTo(0f, 0f)
                    horizontalLineTo(24f)
                    verticalLineTo(24f)
                    horizontalLineTo(0f)
                    verticalLineTo(0f)
                    close()
                }
            }
            group {
                path(
                    fill = SolidColor(Color.Black)
                ) {
                    moveTo(11f, 21f)
                    horizontalLineToRelative(-1f)
                    lineToRelative(1f, -7f)
                    horizontalLineTo(7.5f)
                    curveToRelative(-0.88f, 0f, -0.33f, -0.75f, -0.31f, -0.78f)
                    curveTo(8.48f, 10.94f, 10.42f, 7.54f, 13.01f, 3f)
                    horizontalLineToRelative(1f)
                    lineToRelative(-1f, 7f)
                    horizontalLineToRelative(3.51f)
                    curveToRelative(0.4f, 0f, 0.62f, 0.19f, 0.4f, 0.66f)
                    curveTo(12.97f, 17.55f, 11f, 21f, 11f, 21f)
                    close()
                }
            }
        }.build()

        return _MaterialIconsBolt!!
    }

private var _MaterialIconsBolt: ImageVector? = null







