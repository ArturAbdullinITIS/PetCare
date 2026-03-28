package ru.tbank.petcare.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import ru.tbank.petcare.R

@Serializable
sealed interface Route {
    @Serializable
    data class PetProfile(val petId: String) : Route

    @Serializable
    data object AddPet : Route

    @Serializable
    data object EditPet : Route
}

@Serializable
sealed interface NavigationBarRoute : Route {
    @Serializable
    data object MyPets : NavigationBarRoute

    @Serializable
    data object Public : NavigationBarRoute

    @Serializable
    data object PetRunner : NavigationBarRoute

    @Serializable
    data object Profile : NavigationBarRoute
}

data class RouteConfig(
    @DrawableRes val icon: Int,
    @StringRes val titleRes: Int,
    val route: Route
)
val bottomBarRoutesList = listOf(
    RouteConfig(
        icon = R.drawable.ic_pet_care_main,
        titleRes = R.string.my_pets_screen_title,
        route = NavigationBarRoute.MyPets
    ),
    RouteConfig(
        icon = R.drawable.ic_public,
        titleRes = R.string.public_profiles_title,
        route = NavigationBarRoute.Public
    ),
    RouteConfig(
        icon = R.drawable.ic_pet_runner,
        titleRes = R.string.pet_runner_title,
        route = NavigationBarRoute.PetRunner
    ),
    RouteConfig(
        icon = R.drawable.ic_profile,
        titleRes = R.string.profile_title,
        route = NavigationBarRoute.Profile
    )
)

fun Route.getConfig(): RouteConfig? {
    return bottomBarRoutesList.find { it.route == this }
}


