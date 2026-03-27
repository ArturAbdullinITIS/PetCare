package ru.tbank.petcare.presentation.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

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

val SparklesIconStatus: ImageVector
    get() {
        if (_HeroiconsSparkles != null) return _HeroiconsSparkles!!

        _HeroiconsSparkles = ImageVector.Builder(
            name = "sparkles",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 1.5f,
                strokeLineJoin = StrokeJoin.Miter
            ) {
                moveTo(9.813f, 15.904f)
                lineTo(9f, 18.75f)
                lineToRelative(-0.813f, -2.846f)
                arcToRelative(4.5f, 4.5f, 0f, false, false, -3.09f, -3.09f)
                lineTo(2.25f, 12f)
                lineToRelative(2.846f, -0.813f)
                arcToRelative(4.5f, 4.5f, 0f, false, false, 3.09f, -3.09f)
                lineTo(9f, 5.25f)
                lineToRelative(0.813f, 2.846f)
                arcToRelative(4.5f, 4.5f, 0f, false, false, 3.09f, 3.09f)
                lineTo(15.75f, 12f)
                lineToRelative(-2.846f, 0.813f)
                arcToRelative(4.5f, 4.5f, 0f, false, false, -3.09f, 3.09f)
                close()
                moveTo(18.259f, 8.715f)
                lineTo(18f, 9.75f)
                lineToRelative(-0.259f, -1.035f)
                arcToRelative(3.375f, 3.375f, 0f, false, false, -2.455f, -2.456f)
                lineTo(14.25f, 6f)
                lineToRelative(1.036f, -0.259f)
                arcToRelative(3.375f, 3.375f, 0f, false, false, 2.455f, -2.456f)
                lineTo(18f, 2.25f)
                lineToRelative(0.259f, 1.035f)
                arcToRelative(3.375f, 3.375f, 0f, false, false, 2.456f, 2.456f)
                lineTo(21.75f, 6f)
                lineToRelative(-1.035f, 0.259f)
                arcToRelative(3.375f, 3.375f, 0f, false, false, -2.456f, 2.456f)
                close()
                moveTo(16.894f, 20.567f)
                lineTo(16.5f, 21.75f)
                lineToRelative(-0.394f, -1.183f)
                arcToRelative(2.25f, 2.25f, 0f, false, false, -1.423f, -1.423f)
                lineTo(13.5f, 18.75f)
                lineToRelative(1.183f, -0.394f)
                arcToRelative(2.25f, 2.25f, 0f, false, false, 1.423f, -1.423f)
                lineToRelative(0.394f, -1.183f)
                lineToRelative(0.394f, 1.183f)
                arcToRelative(2.25f, 2.25f, 0f, false, false, 1.423f, 1.423f)
                lineToRelative(1.183f, 0.394f)
                lineToRelative(-1.183f, 0.394f)
                arcToRelative(2.25f, 2.25f, 0f, false, false, -1.423f, 1.423f)
                close()
            }
        }.build()

        return _HeroiconsSparkles!!
    }

private var _HeroiconsSparkles: ImageVector? = null




val WalkQuickActionIcon: ImageVector
    get() {
        if (_FontAwesomeDog != null) return _FontAwesomeDog!!

        _FontAwesomeDog = ImageVector.Builder(
            name = "dog",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 576f,
            viewportHeight = 512f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(298.06f, 224f)
                lineTo(448f, 277.55f)
                verticalLineTo(496f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, 16f)
                horizontalLineTo(368f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
                verticalLineTo(384f)
                horizontalLineTo(192f)
                verticalLineTo(496f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, 16f)
                horizontalLineTo(112f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
                verticalLineTo(282.09f)
                curveTo(58.84f, 268.84f, 32f, 233.66f, 32f, 192f)
                arcToRelative(32f, 32f, 0f, false, true, 64f, 0f)
                arcToRelative(32.06f, 32.06f, 0f, false, false, 32f, 32f)
                close()
                moveTo(544f, 112f)
                verticalLineToRelative(32f)
                arcToRelative(64f, 64f, 0f, false, true, -64f, 64f)
                horizontalLineTo(448f)
                verticalLineToRelative(35.58f)
                lineTo(320f, 197.87f)
                verticalLineTo(48f)
                curveToRelative(0f, -14.25f, 17.22f, -21.39f, 27.31f, -11.31f)
                lineTo(374.59f, 64f)
                horizontalLineToRelative(53.63f)
                curveToRelative(10.91f, 0f, 23.75f, 7.92f, 28.62f, 17.69f)
                lineTo(464f, 96f)
                horizontalLineToRelative(64f)
                arcTo(16f, 16f, 0f, false, true, 544f, 112f)
                close()
                moveToRelative(-112f, 0f)
                arcToRelative(16f, 16f, 0f, true, false, -16f, 16f)
                arcTo(16f, 16f, 0f, false, false, 432f, 112f)
                close()
            }
        }.build()

        return _FontAwesomeDog!!
    }

private var _FontAwesomeDog: ImageVector? = null

val GroomingQuickActionIcon: ImageVector
    get() {
        if (_RadixScissors != null) return _RadixScissors!!

        _RadixScissors = ImageVector.Builder(
            name = "scissors",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 15f,
            viewportHeight = 15f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(4.77552f, 9.59498f)
                curveTo(4.88703f, 9.87512f, 4.95032f, 10.1804f, 4.95032f, 10.5002f)
                curveTo(4.95011f, 11.8531f, 3.85302f, 12.9503f, 2.50015f, 12.9504f)
                curveTo(1.1472f, 12.9504f, 0.0501965f, 11.8531f, 0.0499872f, 10.5002f)
                curveTo(0.0499872f, 9.14716f, 1.14707f, 8.05007f, 2.50015f, 8.05007f)
                curveTo(3.17229f, 8.05012f, 3.78109f, 8.32067f, 4.22376f, 8.75905f)
                lineTo(6.42686f, 7.28739f)
                lineTo(6.67979f, 6.38017f)
                curveTo(6.75165f, 6.12313f, 6.92313f, 5.9055f, 7.15635f, 5.77569f)
                lineTo(10.2305f, 4.06477f)
                curveTo(11.4087f, 3.40911f, 12.7079f, 2.99869f, 14.0488f, 2.85775f)
                lineTo(15f, 2.75717f)
                lineTo(4.77552f, 9.59498f)
                close()
                moveTo(15f, 12.2405f)
                lineTo(14.0488f, 12.1399f)
                curveTo(12.7082f, 11.999f, 11.4095f, 11.5883f, 10.2315f, 10.9328f)
                lineTo(7.15635f, 9.22193f)
                curveTo(7.15236f, 9.21971f, 7.14856f, 9.21644f, 7.14463f, 9.21412f)
                lineTo(8.81062f, 8.09988f)
                lineTo(15f, 12.2405f)
                close()
                moveTo(2.50015f, 8.95045f)
                curveTo(1.64412f, 8.95045f, 0.950367f, 9.64421f, 0.950367f, 10.5002f)
                curveTo(0.950576f, 11.3561f, 1.64425f, 12.05f, 2.50015f, 12.05f)
                curveTo(3.35597f, 12.0499f, 4.04973f, 11.356f, 4.04994f, 10.5002f)
                curveTo(4.04994f, 9.64427f, 3.3561f, 8.95056f, 2.50015f, 8.95045f)
                close()
                moveTo(2.50015f, 2.03452f)
                curveTo(3.85314f, 2.03463f, 4.95032f, 3.13167f, 4.95032f, 4.48469f)
                curveTo(4.95029f, 4.80808f, 4.88549f, 5.11608f, 4.77161f, 5.39874f)
                lineTo(5.73742f, 6.04521f)
                lineTo(5.71593f, 6.1126f)
                lineTo(5.56262f, 6.66239f)
                lineTo(5.21008f, 6.89774f)
                lineTo(4.214f, 6.23271f)
                curveTo(3.77206f, 6.66606f, 3.16792f, 6.9348f, 2.50015f, 6.93485f)
                curveTo(1.14714f, 6.93485f, 0.0500929f, 5.83768f, 0.0499872f, 4.48469f)
                curveTo(0.0499872f, 3.13161f, 1.14707f, 2.03452f, 2.50015f, 2.03452f)
                close()
                moveTo(2.50015f, 2.9349f)
                curveTo(1.64412f, 2.9349f, 0.950367f, 3.62866f, 0.950367f, 4.48469f)
                curveTo(0.950472f, 5.34063f, 1.64419f, 6.03447f, 2.50015f, 6.03447f)
                curveTo(3.35603f, 6.03437f, 4.04983f, 5.34056f, 4.04994f, 4.48469f)
                curveTo(4.04994f, 3.62872f, 3.35609f, 2.93501f, 2.50015f, 2.9349f)
                close()
            }
        }.build()

        return _RadixScissors!!
    }

private var _RadixScissors: ImageVector? = null

val VetQuickActionIcon: ImageVector
    get() {
        if (_PhosphorFirstAidKit != null) return _PhosphorFirstAidKit!!

        _PhosphorFirstAidKit = ImageVector.Builder(
            name = "first-aid-kit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(216f, 56f)
                horizontalLineTo(176f)
                verticalLineTo(48f)
                arcToRelative(24f, 24f, 0f, false, false, -24f, -24f)
                horizontalLineTo(104f)
                arcTo(24f, 24f, 0f, false, false, 80f, 48f)
                verticalLineToRelative(8f)
                horizontalLineTo(40f)
                arcTo(16f, 16f, 0f, false, false, 24f, 72f)
                verticalLineTo(200f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
                horizontalLineTo(216f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
                verticalLineTo(72f)
                arcTo(16f, 16f, 0f, false, false, 216f, 56f)
                close()
                moveTo(96f, 48f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, -8f)
                horizontalLineToRelative(48f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, 8f)
                verticalLineToRelative(8f)
                horizontalLineTo(96f)
                close()
                moveTo(216f, 200f)
                horizontalLineTo(40f)
                verticalLineTo(72f)
                horizontalLineTo(216f)
                verticalLineTo(200f)
                close()
                moveToRelative(-56f, -64f)
                arcToRelative(8f, 8f, 0f, false, true, -8f, 8f)
                horizontalLineTo(136f)
                verticalLineToRelative(16f)
                arcToRelative(8f, 8f, 0f, false, true, -16f, 0f)
                verticalLineTo(144f)
                horizontalLineTo(104f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, -16f)
                horizontalLineToRelative(16f)
                verticalLineTo(112f)
                arcToRelative(8f, 8f, 0f, false, true, 16f, 0f)
                verticalLineToRelative(16f)
                horizontalLineToRelative(16f)
                arcTo(8f, 8f, 0f, false, true, 160f, 136f)
                close()
            }
        }.build()

        return _PhosphorFirstAidKit!!
    }

private var _PhosphorFirstAidKit: ImageVector? = null

val PetTipsIcon: ImageVector
    get() {
        if (_MaterialSymbolsLightbulb != null) return _MaterialSymbolsLightbulb!!

        _MaterialSymbolsLightbulb = ImageVector.Builder(
            name = "lightbulb",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(480f, 880f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(400f, 800f)
                horizontalLineToRelative(160f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(480f, 880f)
                close()
                moveTo(320f, 760f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(320f)
                verticalLineToRelative(80f)
                horizontalLineTo(320f)
                close()
                moveToRelative(10f, -120f)
                quadToRelative(-69f, -41f, -109.5f, -110f)
                reflectiveQuadTo(180f, 380f)
                quadToRelative(0f, -125f, 87.5f, -212.5f)
                reflectiveQuadTo(480f, 80f)
                quadToRelative(125f, 0f, 212.5f, 87.5f)
                reflectiveQuadTo(780f, 380f)
                quadToRelative(0f, 81f, -40.5f, 150f)
                reflectiveQuadTo(630f, 640f)
                horizontalLineTo(330f)
                close()
                moveToRelative(24f, -80f)
                horizontalLineToRelative(252f)
                quadToRelative(45f, -32f, 69.5f, -79f)
                reflectiveQuadTo(700f, 380f)
                quadToRelative(0f, -92f, -64f, -156f)
                reflectiveQuadToRelative(-156f, -64f)
                quadToRelative(-92f, 0f, -156f, 64f)
                reflectiveQuadToRelative(-64f, 156f)
                quadToRelative(0f, 54f, 24.5f, 101f)
                reflectiveQuadToRelative(69.5f, 79f)
                close()
                moveToRelative(126f, 0f)
                close()
            }
        }.build()

        return _MaterialSymbolsLightbulb!!
    }

private var _MaterialSymbolsLightbulb: ImageVector? = null










